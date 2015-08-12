package com.espn.esd.propsutil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Servlet implementation class PropsUtilServlet
 */
public class PropsUtilServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final Logger LOG = Logger.getLogger(PropsUtilServlet.class);

	public static final String JSON_CONTENT = "application/json";

	/**
	 * Default constructor.
	 */
	public PropsUtilServlet() {
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// do nothing
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		Set<Property> fileOneProperties = new HashSet<Property>();
		Set<Property> fileTwoProperties = new HashSet<Property>();
		ResultsDTO results = new ResultsDTO();

		StringBuffer sb = new StringBuffer();
		parseRequest(request.getReader(), sb);
		JSONObject fileCompare = generateJSONObject(sb);
		parseFile((String) fileCompare.get("file1"), fileOneProperties,
				results.getFileOneDuplicates());
		parseFile((String) fileCompare.get("file2"), fileTwoProperties,
				results.getFileTwoDuplicates());
		results.getFileOneDeficiencies().addAll(fileTwoProperties);
		results.getFileOneDeficiencies().removeAll(fileOneProperties);
		results.getFileTwoDeficiencies().addAll(fileOneProperties);
		results.getFileTwoDeficiencies().removeAll(fileTwoProperties);
		if (LOG.isDebugEnabled()) {
			logResults(results);
		}

		ObjectMapper mapper = new ObjectMapper();
		String jsonResult = mapper.writeValueAsString(results);
		if (LOG.isDebugEnabled()) {
			LOG.debug(jsonResult);
		}

		response.setContentType(JSON_CONTENT);
		PrintWriter out = response.getWriter();
		out.print(jsonResult);
		out.flush();
		out.close();
	}

	private void parseRequest(BufferedReader reader, StringBuffer sb) {
		try {
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private JSONObject generateJSONObject(StringBuffer sb) {
		JSONParser parser = new JSONParser();
		JSONObject jsonObject = null;

		try {
			jsonObject = (JSONObject) parser.parse(sb.toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return jsonObject;
	}

	private void parseFile(String file, Set<Property> fileProperties,
			List<Property> duplicates) {
		String lines[] = file.split("\\r?\\n");
		boolean duplicate = false;

		for (int i = 0; i < lines.length; i++) {
			if (!lines[i].startsWith("#") && !lines[i].equalsIgnoreCase("")) {
				Property property = new Property();
				property.setName(lines[i].substring(0, lines[i].indexOf('=')));
				duplicate = !fileProperties.add(property);
				if (duplicate) {
					duplicates.add(property);
				}
			}
		}
	}

	private void logResults(ResultsDTO results) {
		LOG.debug("\n");
		LOG.debug("### Duplicate Properties ###");
		for (Property p : results.getFileOneDuplicates()) {
			LOG.debug("[" + p.getName() + "] found in File 1");
		}

		for (Property p : results.getFileTwoDuplicates()) {
			LOG.debug("[" + p.getName() + "] found in File 2");
		}

		LOG.debug("\n");
		LOG.debug("### File One Deficiencies ###");
		for (Property p : results.getFileOneDeficiencies()) {
			LOG.debug(p.getName());
		}

		LOG.debug("\n");
		LOG.debug("### File Two Deficiencies ###");
		for (Property p : results.getFileTwoDeficiencies()) {
			LOG.debug(p.getName());
		}
	}

}
