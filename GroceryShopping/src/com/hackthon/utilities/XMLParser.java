/**
 * @author RAMESH
 *
 */
package com.hackthon.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XMLParser {

	private static XMLParser parserObj;

	private String KEY = "a785cac0f3";
	private String TAG_STATES = "StoreState";
	private String TAG_CITIES = "StoreCity";
	private String TAG_STORES = "Store";

	private String BASE_URL = "http://www.supermarketapi.com/api.asmx/";

	public String ALLSTATES_URL = BASE_URL + "AllUSStates";
	public String ALLCITIES_URL = BASE_URL + "CitiesByState?APIKEY=" + KEY
			+ "&SelectedState=";
	public String ALLSTORES_URL = BASE_URL + "StoresByCityState?APIKEY=" + KEY;

	private String ITEM_SEARCH_BASE_URL = BASE_URL
			+ "COMMERCIAL_SearchByProductName?APIKEY=" + KEY + "&ItemName=";

	private XMLParser() {

	}

	public static XMLParser getInstance() {
		if (parserObj == null)
			parserObj = new XMLParser();

		return parserObj;
	}

	public Document getDocumentFromUrlData(String url) {

		Document doc = null;
		try {
			URL urlToRead = new URL(url);

			HttpURLConnection connection = (HttpURLConnection) (urlToRead)
					.openConnection();

			connection.setRequestMethod("GET");
			connection.connect();
			InputStream inputstream = connection.getInputStream();

			BufferedReader in = new BufferedReader(new InputStreamReader(
					inputstream));

			StringBuilder dataRead = new StringBuilder();
			String inputLine = null;
			while ((inputLine = in.readLine()) != null) {
				dataRead.append(inputLine);
			}

			in.close();

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(dataRead.toString()));
			doc = db.parse(is);
			doc.getDocumentElement().normalize();

		} catch (MalformedURLException e) {
			System.out.println("Error: " + e.getMessage());
		} catch (ParserConfigurationException e) {
		} catch (SAXException e) {
			System.out.println("Error: " + e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return doc;
	}

	private ArrayList<StoreStates> parseStoreStates() {
		ArrayList<StoreStates> arrayOfStates = new ArrayList<StoreStates>();

		Document doc = getDocumentFromUrlData(ALLSTATES_URL);

		NodeList nList = doc.getElementsByTagName(TAG_STATES);
		for (int temp = 0; temp < nList.getLength(); temp++) {

			Node nNode = nList.item(temp);
			NodeList childNodeList = nNode.getChildNodes();
			StoreStates obj = new StoreStates();

			for (int i = 0; i < childNodeList.getLength(); i++) {
				if (childNodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {
					Node eElement = (Node) childNodeList.item(i);
					obj.setState(eElement.getTextContent());
				}
			}
			arrayOfStates.add(obj);
		}

		return arrayOfStates;
	}

	public ArrayList<StoreStates> getStatesList() {
		return parseStoreStates();
	}

	private ArrayList<StoreCity> parseStoreCities(String state) {

		ArrayList<StoreCity> arrayofCities = new ArrayList<StoreCity>();

		Document doc = getDocumentFromUrlData(ALLCITIES_URL + state);

		if (doc == null)
			return arrayofCities;

		NodeList nList = doc.getElementsByTagName(TAG_CITIES);

		for (int temp = 0; temp < nList.getLength(); temp++) {

			Node nNode = nList.item(temp);
			NodeList nl = nNode.getChildNodes();
			StoreCity obj = new StoreCity();

			for (int i = 0; i < nl.getLength(); i++) {
				if (nl.item(i).getNodeType() == Node.ELEMENT_NODE
						&& nl.item(i).getNodeName().equalsIgnoreCase("city")) {
					Node eElement = (Node) nl.item(i);
					obj.setCity(eElement.getTextContent());
				}
			}
			arrayofCities.add(obj);
		}
		return arrayofCities;
	}

	public ArrayList<StoreCity> getCitiesList(String state) {
		return parseStoreCities(state);
	}

	public ArrayList<Stores> getStores(String city, String state) {
		return parseStores(city, state);
	}

	private ArrayList<Stores> parseStores(String city, String state) {
		ArrayList<Stores> arrayofStores = new ArrayList<Stores>();

		String url = ALLSTORES_URL + "&SelectedCity=" + city
				+ "&SelectedState=" + state;

		Document doc = getDocumentFromUrlData(url);

		if (doc == null)
			return arrayofStores;

		NodeList nList = doc.getElementsByTagName(TAG_STORES);

		for (int temp = 0; temp < nList.getLength(); temp++) {

			Node nNode = nList.item(temp);
			NodeList nl = nNode.getChildNodes();
			Stores obj = new Stores();

			for (int i = 0; i < nl.getLength(); i++) {

				if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
					Node eElement = (Node) nl.item(i);
					if (eElement.getNodeName().equalsIgnoreCase("Storename")) {
						obj.setStoreName(eElement.getTextContent().trim());
					}

					else if (eElement.getNodeName().equalsIgnoreCase("Address"))
						obj.setAddress(eElement.getTextContent());

					else if (eElement.getNodeName().equalsIgnoreCase("City"))
						obj.setCity(eElement.getTextContent());

					else if (eElement.getNodeName().equalsIgnoreCase("State"))
						obj.setState(eElement.getTextContent());

					else if (eElement.getNodeName().equalsIgnoreCase("Zip"))
						obj.setZip(Integer.parseInt(eElement.getTextContent()));

					else if (eElement.getNodeName().equalsIgnoreCase("StoreId"))
						obj.setStoreId(eElement.getTextContent());
				}
			}
			arrayofStores.add(obj);
		}

		return arrayofStores;
	}

	public ArrayList<ItemDetails> getItemList(String itemToSearch) {
		return parseItemList(itemToSearch);
	}

	private ArrayList<ItemDetails> parseItemList(String itemToSearch) {
		ArrayList<ItemDetails> itemList = new ArrayList<ItemDetails>();

		Document doc = getDocumentFromUrlData(ITEM_SEARCH_BASE_URL
				+ itemToSearch);

		if (doc == null)
			return itemList;

		NodeList nList = doc.getElementsByTagName("Product_Commercial");

		for (int temp = 0; temp < nList.getLength(); temp++) {

			Node nNode = nList.item(temp);
			NodeList nl = nNode.getChildNodes();

			ItemDetails itemDetails = new ItemDetails();

			for (int i = 0; i < nl.getLength(); i++) {

				if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
					Node eElement = (Node) nl.item(i);
					if (eElement.getNodeName().equalsIgnoreCase("Itemname")) {
						itemDetails.setName(eElement.getTextContent());
					} else if (eElement.getNodeName()
							.equalsIgnoreCase("ItemID"))
						itemDetails.setID(eElement.getTextContent());

					else if (eElement.getNodeName().equalsIgnoreCase(
							"ItemDescription"))
						itemDetails.setDescription(eElement.getTextContent());

					else if (eElement.getNodeName().equalsIgnoreCase(
							"ItemCategory"))
						itemDetails.setCategory(eElement.getTextContent());

					else if (eElement.getNodeName().equalsIgnoreCase(
							"ItemImage"))
						itemDetails.setImageLocation(eElement.getTextContent());

					else if (eElement.getNodeName().equalsIgnoreCase("Pricing")
							&& eElement.getTextContent() != "NOPRICE") {
						try {
							itemDetails.setPricing(Double.parseDouble(eElement
									.getTextContent()));
						} catch (Exception e) {
							itemDetails.setPricing(0.0);
						}
					}

					else if (eElement.getNodeName().equalsIgnoreCase(
							"AisleNumber"))
						itemDetails.setAisleNumber(eElement.getTextContent());
				}
			}
			itemList.add(itemDetails);
		}

		return itemList;
	}
}
