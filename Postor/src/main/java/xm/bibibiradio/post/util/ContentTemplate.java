package xm.bibibiradio.post.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xm.bibibiradio.post.biz.PostResourceData;

public class ContentTemplate {
	private static Map<String, String> templates;
	private static SimpleDateFormat dateFormat;
	static {
		templates = new HashMap<String, String>();
		String template = "<table id=\"table_bug_report\" class=\"table table-striped table-bordered table-hover\">            <thead>                <tr>                    <th>type</th>                    <th>level</th>                    <th>time</th>                    <th>Src IP</th>                    <th>Src app</th>                    <th>target</th>                    <th>row</th>                    <th>count</th>                    <th>DB user</th>                    <th>way</th>                    <th>detail</th>                                      </tr>            </thead>                                                            <tbody>                                                 <tr>                                                   <td>%s</td>                            <td>%s</td>                            <td>%s</td>                            <td>%s</td>                            <td>%s</td>                            <td>%s</td>                            <td>%s</td>                            <td>%s</td>                            <td>%s</td>                                          <td>log</td>                            <td>%s</td>                                                                                 </tr></tbody></table>";
		templates.put("getAlertContent", template);
		dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	}

	public static String getAlertContent(String category, int level, Date time,
			String srcIp, String appName, String dstIp, long rowNum,
			long visitCount, String userName, String detail) {
		return String.format(templates.get("getAlertContent"), category,
				Integer.toString(level), dateFormat.format(time), srcIp,
				appName, dstIp, Long.toString(rowNum),
				Long.toString(visitCount), userName, detail);
	}

	private static String getBilibiliResourceContent(
			List<PostResourceData> postDatas) {
		String head = "<table>" + "<tr>" + "<th>缩略图</th>" + "<th>标题</th>"
				+ "<th>作者</th>" + "<th>点击量</th>" + "<th>评论量</th>"
				+ "<th>分数</th>" + "<th>创建时间</th>" + "</tr>";
		String loopDodyTemplate = "<tr><td><img src=\"%s\" /></td>"
				+ "<td><a href=\"%s\">%s</a></td>"
				+ "<td><a href=\"%s\">%s</a></td>" + "<td>%s</td>"
				+ "<td>%s</td>" + "<td>%s</td>" + "<td>%s</td></tr>";
		String end = "</table>";

		StringBuilder sb = new StringBuilder();

		for (PostResourceData postResourceData : postDatas) {
			sb.append(String.format(loopDodyTemplate,
					postResourceData.getResourceShowUrl(),
					postResourceData.getResourceUrl(),
					postResourceData.getTitle(),
					postResourceData.getAuthorUrl(),
					postResourceData.getAuthorName(), postResourceData.getPv(),
					postResourceData.getCommentNum(),
					postResourceData.getScore(),
					dateFormat.format(postResourceData.getCreateDate())));
		}

		String body = sb.toString();

		return head + body + end;
	}

	private static String getCveResourceContent(List<PostResourceData> postDatas) {
		String head = "<table>" + "<tr>" + "<th>标题</th>" + "<th>漏洞分</th>"
				+ "<th>漏洞描述</th>" + "<th>创建时间</th>" + "</tr>";
		String loopDodyTemplate = "<tr><td><a href=\"%s\" target=\"_blank\">%s</a></td>" + "<td>%s</td>"
				+ "<td>%s</td>" + "<td>%s</td></tr>";
		String end = "</table>";

		StringBuilder sb = new StringBuilder();

		for (PostResourceData postResourceData : postDatas) {
			sb.append(String.format(loopDodyTemplate,
					postResourceData.getResourceUrl(),
					postResourceData.getTitle(),
					postResourceData.getScore(),
					postResourceData.getrTags(),
					dateFormat.format(postResourceData.getCreateDate())));
		}

		String body = sb.toString();

		return head + body + end;
	}

	public static String getResourcesContent(List<PostResourceData> postDatas,
			int type) {
		if (type == 0) {
			return ContentTemplate.getBilibiliResourceContent(postDatas);
		} else if (type == 1) {
			return ContentTemplate.getCveResourceContent(postDatas);
		}

		return null;
	}
}
