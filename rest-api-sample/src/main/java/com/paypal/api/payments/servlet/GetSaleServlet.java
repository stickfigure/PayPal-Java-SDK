// #Get Details of a Sale Transaction Sample
// This sample code demonstrates how you can retrieve 
// details of completed Sale Transaction.
// API used: /v1/payments/sale/{sale-id}
package com.paypal.api.payments.servlet;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.paypal.api.payments.Sale;
import com.paypal.api.payments.util.Configuration;
import com.paypal.core.rest.APIContext;
import com.paypal.core.rest.OAuthTokenCredential;
import com.paypal.core.rest.PayPalRESTException;

/**
 * @author lvairamani
 * 
 */
public class GetSaleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = Logger.getLogger(GetSaleServlet.class);

	public void init(ServletConfig servletConfig) throws ServletException {
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	// # Get Sale By SaleID Sample how to get details about a sale.
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		APIContext apiContext = null;
		String accessToken = null;
		try {
			// ###DynamicConfiguration
			// Retrieve the dynamic configuration map
			// containing only the mode parameter at
			// the least
			Map<String, String> configurationMap = Configuration
					.getConfigurationMap();

			// Retrieve the client credentials
			// containing the clientID and
			// clientSecret
			Map<String, String> clientCredentials = Configuration
					.getClientCredentials();

			// ###AccessToken
			// Retrieve the access token from
			// OAuthTokenCredential by passing in
			// ClientID and ClientSecret
			// It is not mandatory to generate Access Token on a per call basis.
			// Typically the access token can be generated once and
			// reused within the expiry window
			accessToken = new OAuthTokenCredential(
					clientCredentials.get("clientID"),
					clientCredentials.get("clientSecret"), configurationMap)
					.getAccessToken();

			// ### Api Context
			// Pass in a `ApiContext` object to authenticate
			// the call and to send a unique request id
			// (that ensures idempotency). The SDK generates
			// a request id if you do not pass one explicitly.
			apiContext = new APIContext(accessToken);
			apiContext.setConfigurationMap(configurationMap);
			// Use this variant if you want to pass in a request id
			// that is meaningful in your application, ideally
			// a order id.
			/*
			 * String requestId = Long.toString(System.nanoTime());
			 * APIContext apiContext = new APIContext(accessToken, requestId));
			 */

			// Pass an APIContext and the ID of the sale
			// transaction from your payment resource.
			Sale sale = Sale.get(apiContext, "03W403310B593121A");
			LOGGER.info("Sale amount : " + sale.getAmount() + " for saleID : "
					+ sale.getId());
			req.setAttribute("response", Sale.getLastResponse());
		} catch (PayPalRESTException e) {
			req.setAttribute("error", e.getMessage());
		}
		req.getRequestDispatcher("response.jsp").forward(req, resp);
	}

}
