package edu.java.generics_collections;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import org.xml.sax.SAXException;

public class Generics_Collections {

	public static void main(String[] args) throws ParserConfigurationException, ParseException {
		// TODO Auto-generated method stub
		File xmlFile = new File("Dictionary.xml");
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		try {
			Document doc = builder.parse(xmlFile);
			doc.getDocumentElement().normalize();
			int sel;
			List<String> favWords = new ArrayList<String>();
			ReadFavoriteWords(favWords);

			do {
				sel = 5;
				System.out.println("----------------------------");
				System.out.println("1.Tra cuu tu dien Anh-Viet");
				System.out.println("2.Tra cuu tu dien Viet-Anh");
				System.out.println("3.Danh sach tu yeu thich");
				System.out.println("4.Thong ke tan suat cac tu da duoc tra cuu");
				System.out.println("5.Thoat");
				System.out.print("Nhap yeu cau: ");
				Scanner sc = new Scanner(System.in);
				sel = sc.nextInt();
				if (sel == 1 || sel == 2) {

					String attributeValue;
					if (sel == 1) {
						attributeValue = "en-vi";
					} else {
						attributeValue = "vi-en";
					}
					String query;
					System.out.print("Nhap tu muon tra cuu: ");
					sc.nextLine();

					query = sc.nextLine();
					//System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
					NodeList nList = doc.getElementsByTagName("dictionary");
					int flag = 0;
					for (int temp = 0; temp < nList.getLength(); temp++) {
						Node nNode = nList.item(temp);
						//System.out.println("Current Element :" + nNode.getNodeName());
						if (nNode.getNodeType() == Node.ELEMENT_NODE) {
							Element eElement = (Element) nNode;
							if (attributeValue.equalsIgnoreCase(eElement.getAttribute("language"))) {
								NodeList textNodes = eElement.getElementsByTagName("set");

								for (int temp1 = 0; temp1 < textNodes.getLength(); temp1++) {
									Node nNode1 = textNodes.item(temp1);
									//System.out.println("Current Element :" + nNode1.getNodeName());
									if (nNode1.getNodeType() == Node.ELEMENT_NODE) {
										Element eElement1 = (Element) nNode1;
										if (eElement1.getElementsByTagName("word").item(0).getTextContent()
												.equalsIgnoreCase(query)) {
											System.out.print(
													eElement1.getElementsByTagName("word").item(0).getTextContent()
															+ ": ");
											System.out.println(
													eElement1.getElementsByTagName("meaning").item(0).getTextContent());
											flag = 1;
											String word = eElement1.getElementsByTagName("word").item(0)
													.getTextContent();
											// Ghi vao file log
											WriteIntoLog(word);
											if (favWords.contains(word)) {
												System.out.println("TU NAM TRONG DANH SACH YEU THICH CUA BAN");
												break;
											}
											
											char c;
											do {
												System.out.print("Ban co muon luu tu vao muc yeu thich (y/n); ");
												c = (char) System.in.read();
												if (c == 'y' || c == 'Y') {

													favWords.add(eElement1.getElementsByTagName("word").item(0)
															.getTextContent());
													// Ghi danh sach tu yeu thich vua cap nhat vao file
													AddWordToFavorite(favWords);

												} else if (c == 'n' || c == 'N') {
													break;
												}
											} while (c != 'y' && c != 'n' && c != 'Y' && c != 'N');
											break;
										}
									}
								}
								if (flag == 0) {
									System.out.println("KHONG THAY TU CAN TIM");
									break;
								}

							}
						}
					}

				} else if (sel == 3) {
					int order;
					do {

						order = 3;
						List<String> sort = new ArrayList<String>(favWords);

						System.out.println("-------------DANH SACH TU YEU THICH---------------");
						System.out.println("1.SAP XEP THEO THU TU A-Z");
						System.out.println("2.SAP XEP THEO THU TU Z-A");
						System.out.println("3.Tro ve menu chinh");
						System.out.print("Nhap yeu cau: ");
						order = sc.nextInt();

						if (order == 1) {
							Collections.sort(sort);
							System.out.println("--------A-Z--------");
							for (String itr : sort) {
								System.out.println(itr);
							}
						} else if (order == 2) {
							Collections.sort(sort, Collections.reverseOrder());
							System.out.println("--------Z-A--------");
							for (String itr : sort) {
								System.out.println(itr);
							}
						}
					} while (order != 3);

				} else if (sel == 4) {

					SimpleDateFormat objSDF = new SimpleDateFormat("dd/MM/yyyy");
					sc.nextLine();
					System.out.println("-------------THONG KE CAC TU DA TRA CUU---------------");
					System.out.print("Tu ngay (theo dinh dang dd/mm/yy): ");
					String dateString1 = sc.nextLine();

					System.out.print("den ngay (theo dinh dang dd/mm/yy): ");
					String dateString2 = sc.nextLine();
					Date date1 = objSDF.parse(dateString1);
					Date date2 = objSDF.parse(dateString2);

					Map<String, Integer> freqMap = new HashMap<String, Integer>();
					Scanner sc2 = new Scanner(new File("log.txt"));
					String key;
					String word;
					while (sc2.hasNext()) {
						key = sc2.nextLine();
						word = sc2.nextLine();
						String strDateFormat = "dd-MMM-yyyy"; // Date format is Specified
						objSDF = new SimpleDateFormat(strDateFormat);
						Date date3 = objSDF.parse(key);
						if (date3.compareTo(date1) >= 0 && date3.compareTo(date2) <= 0) {
							String keyV = word;
							if (freqMap.containsKey(keyV)) {

								freqMap.put(keyV, freqMap.get(keyV) + 1);
							} else {
								freqMap.put(keyV, 1);
							}
						}
					}
					// int a = 0;
					// Map<String,Integer> freq = new HashMap<String,Integer>();
					// for(Map.Entry<Date, String> entry:map.entrySet()) {
					// if (entry.getKey().compareTo(date) == 0){
					// String keyV = entry.getValue();
					// if(freq.containsKey(keyV)){
					//
					// freq.put(keyV,freq.get(keyV) + 1);
					// }
					// else {
					// freq.put(keyV, 1);
					// }
					// }
					// }
					for (Map.Entry<String, Integer> entry : freqMap.entrySet()) {
						System.out.println(entry.getKey() + ": " + entry.getValue() + " lan");
					}
				}
			} while (sel != 5);

		} catch (

		SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	static void WriteIntoLog(String word) throws ParseException, IOException {

		//String strDateFormat1 = "dd/MMM/yyyy"; // Date format is Specified
		SimpleDateFormat objSDF = new SimpleDateFormat("dd-MMM-yyyy");
		String dateString = objSDF.format(new Date());
		Date date = objSDF.parse(dateString);
		//System.out.println(date);

		BufferedWriter fileWriter = new BufferedWriter(new FileWriter("log.txt", true));
		try {

//			String strDateFormat = "dd-MMM-yyyy"; // Date format is Specified
//			objSDF = new SimpleDateFormat(strDateFormat); // Date format string is passed as an
//																			// argument to the Date format
//																			// object
			fileWriter.write(objSDF.format(date));
			fileWriter.newLine(); // Date formatting is applied to the current date
			
			fileWriter.write(word);
			fileWriter.newLine();

			//System.out.println("GHI DU LIEU THANH CONG!!!");

		} catch (Exception e) {
			System.out.println("GHI DU LIEU THAT BAI!!!");
			e.printStackTrace();
		} finally {
			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				System.out.println("ERROR WHILE FLUSHING/CLOSING FILEWRITER !!!");
				e.printStackTrace();
			}
		}
	}
	static void ReadFavoriteWords(List<String> favWords) {
		try {
			Scanner sc2 = new Scanner(new File("Favorite.txt"));
			while (sc2.hasNext()) {
				String word = sc2.nextLine();
				favWords.add(word);
			}
			sc2.close();
			// System.out.println("DOC DU LIEU THANH CONG!!!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// System.out.println("KHONG MO DUOC FILE!!!");
		}
	}
	static void AddWordToFavorite(List<String> favWords){
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter("Favorite.txt");
			for (String itr : favWords) {
				fileWriter.append(itr);
				fileWriter.append(System.getProperty("line.separator"));
				// fileWriter.append(String.format("%n"));
				// System.lineSeparator()
			}
			//System.out.println("GHI DU LIEU THANH CONG!!!");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				System.out.println(
						"ERROR WHILE FLUSHING/CLOSING FILEWRITER !!!");
				e.printStackTrace();
			}
		}
	}

}
