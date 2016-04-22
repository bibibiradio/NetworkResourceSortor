package xm.bibibiradio.mainsystem.webservice.common.views;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.view.AbstractView;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonView extends AbstractView {
	private static ObjectMapper objectMapper = new ObjectMapper();
	final static private Logger logger = Logger.getLogger(JsonView.class);
	@Override
	protected void renderMergedOutputModel(Map model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		response.addHeader("Content-Type", "text/json; charset=utf-8");
		PrintWriter pr = response.getWriter();
		if(pr == null){
			return;
		}
		JsonGenerator jsonGenerator = null;
		try {
            jsonGenerator = objectMapper.getJsonFactory().createGenerator(pr);
        } catch (IOException e) {
            logger.error("error message",e);
        }
		jsonGenerator.writeObject(model);
		jsonGenerator.close();
		//JSONArray
	}

}
