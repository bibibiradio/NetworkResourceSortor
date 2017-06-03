package xm.bibibiradio.mainsystem.spider;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.bibibiradio.httpsender.HttpSender;
import com.bibibiradio.httpsender.HttpSenderImplV1;
import com.bibibiradio.httpsender.ResponseData;

import xm.bibibiradio.mainsystem.beanfactory.MainSystemBeanFactory;
import xm.bibibiradio.mainsystem.dal.ResourceDAO;
import xm.bibibiradio.mainsystem.dal.ResourceData;

public class SpiderCveImpl implements ISpider {
	final static private Logger LOGGER = Logger.getLogger(SpiderCveImpl.class);

	final static private String urlFormat = "http://cve.scap.org.cn/cve_list.php?action=cvss&floor=0&ceil=10&p=%s";

	private ResourceDAO resourceDAOStart;
	private ResourceDAO resourceDAOAfter;
	private Properties conf;
	private HttpSender httpSenderStart;
	private HttpSender httpSenderAfter;
	private SimpleDateFormat cveDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	public List<CveData> getCves(int page, HttpSender httpSender)
			throws Exception {
		String url = String.format(urlFormat, page);

		ResponseData responseData = httpSender.send(url, 0, null, null);

		if (responseData == null)
			throw new Exception("internal error responseData is null");

		if (responseData.getStatusCode() != 200)
			throw new Exception("return code != 200");

		String content = new String(responseData.getResponseContent(), "utf-8");

		List<CveData> cveDatas = new ArrayList<CveData>();
		Document doc = Jsoup.parse(content);
		Elements eles = doc.select("div.entry h2");
		for (Element ele : eles) {
			Element titleEle = ele.select("a").get(0);
			String cveUrl = titleEle.attr("href");
			String cveTitle = titleEle.text();

			Element scoreEle = ele.select("span.tip_plain").get(0);
			// String cveLevel = scoreEle.attr("title");
			String cveScore = scoreEle.text();
			// System.out.println(cveTitle+":"+cveUrl+":"+cveScore+":"+cveLevel);

			Element timeEle = ele.select("span[style=padding-left:20px]")
					.get(0);
			String date = timeEle.text();
			date = date.substring("(发布:".length(), date.length() - 1);

			CveData cveData = new CveData();
			cveData.setTitle(cveTitle);
			cveData.setUrl(cveUrl);
			cveData.setScore(cveScore);
			cveData.setDate(date);

			cveDatas.add(cveData);
			// System.out.println(ele.toString()+"aaaaa\n\naaaaa");
		}

		eles = doc.select("div.entry h3");
		int i = 0;
		for (Element ele : eles) {
			CveData cveData = cveDatas.get(i);
			cveData.setContent(ele.text());
			i++;
			// System.out.println(cveData.getTitle()+":"+cveData.getUrl()+":"+cveData.getScore()+":"+cveData.getContent());
			// System.out.println(ele.toString()+"aaaaa\n\naaaaa");
		}
		return cveDatas;
	}

	@Override
	public void startForward(String configPath) throws Exception {
		// TODO Auto-generated method stub
		while (true) {
			int page = 1;
			boolean isExist = false;
			try {
				while (true) {
					try {
						LOGGER.info("start forward page " + page);
						List<CveData> cveDatas = getCves(page, httpSenderStart);
						for (CveData cveData : cveDatas) {
							try {
								Long rId = resourceDAOStart.selectByrInnerId(cveData.getTitle().hashCode(),2);
								if (rId == null) {
									ResourceData rd = new ResourceData();
									rd.setrSite(2);
									rd.setrType(1);
									rd.setAuthorId(725648);

									rd.setOtherDetail("");
									rd.setrCategory("cve");

									rd.setrCoin(0);
									rd.setrCollect(0);
									rd.setrComment(0);

									rd.setrDanmu(0);
									rd.setrDuration(0);
									rd.setrPv((long) (Double.valueOf(cveData
											.getScore()) * 10));
									rd.setScore(rd.getrPv());
									rd.setrShowUrl("");
									rd.setrUrl(cveData.getUrl());
									if (cveData.getContent().length() > 1000)
										cveData.setContent(cveData.getContent()
												.substring(0, 1000));
									rd.setrTags(cveData.getContent());
									Date date = cveDateFormat.parse(cveData
											.getDate());
									rd.setrGmtCreate(date);
									rd.setrTitle(cveData.getTitle());
									rd.setrInnerId(cveData.getTitle().hashCode());

									resourceDAOStart.insertResource(rd);
								} else {
									isExist = true;
								}
							} catch (Exception ex) {
								LOGGER.error("error", ex);
							}
						}
					} catch (Exception ex) {
						LOGGER.error("error", ex);
					} finally {
						if (isExist) {
							break;
						}

						page++;
					}
				}

			} catch (Exception ex) {
				LOGGER.error("error", ex);
			}

			Thread.sleep(60000);
		}
	}

	@Override
	public void updateNow(String configPath) throws Exception {
		// TODO Auto-generated method stub
		return;
	}

	@Override
	public void startAfter(String configPath) throws Exception {
		// TODO Auto-generated method stub
		String lastTitle = "CVE-1999-0095";
		int page = 3;
		Long rId = resourceDAOAfter.selectByrInnerId(lastTitle.hashCode(),2);
		if (rId != null)
			return;
		while (true) {
			boolean isExistLast = false;
			LOGGER.info("start after page " + page);
			try {
				List<CveData> cveDatas = getCves(page, httpSenderStart);
				for (CveData cveData : cveDatas) {
					try {
						if (cveData.getTitle().equals(lastTitle))
							isExistLast = true;
						rId = resourceDAOAfter.selectByrInnerId(cveData.getTitle().hashCode(),2);
						if (rId == null) {
							ResourceData rd = new ResourceData();
							rd.setrSite(2);
							rd.setrType(1);
							rd.setAuthorId(725648);

							rd.setOtherDetail("");
							rd.setrCategory("cve");

							rd.setrCoin(0);
							rd.setrCollect(0);
							rd.setrComment(0);

							rd.setrDanmu(0);
							rd.setrDuration(0);
							rd.setrPv((long) (Double.valueOf(cveData.getScore()) * 10));
							rd.setScore(rd.getrPv());
							rd.setrShowUrl("");
							rd.setrUrl(cveData.getUrl());
							if (cveData.getContent().length() > 1000)
								cveData.setContent(cveData.getContent()
										.substring(0, 1000));
							rd.setrTags(cveData.getContent());
							Date date = cveDateFormat.parse(cveData.getDate());
							rd.setrGmtCreate(date);
							rd.setrTitle(cveData.getTitle());
							rd.setrInnerId(cveData.getTitle().hashCode());

							resourceDAOStart.insertResource(rd);
						}
					} catch (Exception ex) {
						LOGGER.error("error", ex);
					}
				}

			} catch (Exception ex) {
				LOGGER.error("error", ex);
			} finally {
				if (isExistLast)
					return;
				page++;
			}
		}
	}

	@Override
	public void init(String configPath) throws Exception {
		// TODO Auto-generated method stub
		httpSenderStart = new HttpSenderImplV1();
		httpSenderStart.setCodec(true);
		httpSenderStart.setRetryTime(3);
		httpSenderStart.setSendFreq(3000);
		httpSenderStart.setTimeout(30000);
		httpSenderStart.setSoTimeout(30000);

		httpSenderStart.start();

		httpSenderAfter = new HttpSenderImplV1();
		httpSenderAfter.setCodec(true);
		httpSenderAfter.setRetryTime(3);
		httpSenderAfter.setSendFreq(3000);
		httpSenderAfter.setTimeout(30000);
		httpSenderAfter.setSoTimeout(30000);

		httpSenderAfter.start();

		resourceDAOStart = (ResourceDAO) MainSystemBeanFactory
				.getMainSystemBeanFactory().getBean("resourceDAO");
		resourceDAOAfter = (ResourceDAO) MainSystemBeanFactory
				.getMainSystemBeanFactory().getBean("resourceDAO");
	}

}
