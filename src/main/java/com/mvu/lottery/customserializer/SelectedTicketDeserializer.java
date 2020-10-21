package com.mvu.lottery.customserializer;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import com.jayway.jsonpath.internal.filter.ValueNode.NumberNode;
import com.mvu.lottery.data.model.SelectedTicketData;

public class SelectedTicketDeserializer extends JsonDeserializer<SelectedTicketData> {

	@Override
	public SelectedTicketData deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		
		
		Supplier<RuntimeException> missingDataExceptionGenerator = () -> { throw new RuntimeException("Missing data"); };
		
		
		SelectedTicketData data = new SelectedTicketData();
		
		JsonNode jnode = p.getCodec().readTree(p);
		
		jnode.fieldNames().forEachRemaining(name -> {
			log(">>>field name:"+name);
		});
		
		Date drawingDate = null;
		String pattern = "yyyy-MM-dd'T'hh:mm:ss.SSS'Z'";
		try {
			drawingDate = 
					parseDate(pattern,
					getNodeValue(jnode, "drawingDate").orElseThrow(missingDataExceptionGenerator).asText()
			);
		} catch (Exception e) {
		
			throw new RuntimeException(e);
		}
		
		
		
		JsonNode tickets = 	getNodeValue(jnode, "ticketSet").orElseThrow(missingDataExceptionGenerator)
							 			.get("numberList");
		
		
		List<SelectedTicketData.Ticket> ticketList = new LinkedList<SelectedTicketData.Ticket>();
		
		tickets.elements().forEachRemaining((tjnode) -> {
			SelectedTicketData.Ticket ticket = new SelectedTicketData.Ticket();
			String mega = tjnode.asText();
			final List<String> numList = new LinkedList<String>();
			tjnode.get("number").elements().forEachRemaining((numberNode) -> {
				numList.add(numberNode.asText());
			});
			ticket.setMega(mega);
			ticket.setNumber(numList.toArray(new String[numList.size()]));
			ticketList.add(ticket);
		});
		
		SelectedTicketData.TicketSet ticketSet = new SelectedTicketData.TicketSet();
		ticketSet.setnumberList(ticketList.toArray(  new SelectedTicketData.Ticket[  ticketList.size() ]  ));
	
		data.setDrawingDate(drawingDate);
		data.setTicketSet(ticketSet);
		
		
		return data;
	}
	
	/**
	 * 
	 * @param format
	 * @param dateString
	 * @return
	 * @throws RuntimeException
	 */
	private static Date parseDate(String format, String dateString) throws Exception {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		
		Date d = dateFormat.parse(dateString);
		
		return d;
	}
	
	/**
	 * 
	 * @param node
	 * @param names
	 * @throws Exception
	 */
	private static Optional<JsonNode> getNodeValue(final JsonNode jnode, final String name) throws RuntimeException {
		
		Optional<JsonNode> node = Optional.ofNullable(null);
		
		try
		{
			JsonNode nextNode = jnode.get(name);
			if(nextNode.isMissingNode()) {
				throw new RuntimeException("Missing node:"+name);
			}
			node = Optional.ofNullable(nextNode);
		
		} catch (Exception e) {
			
		}
		
		return node;
	}
	
	/**
	 * 
	 * @param node
	 * @param names
	 * @param processStringValueFunct
	 * @param processIntValueFunct
	 * @param processDoubbleValueFunct
	 * @param processArrayValueFunct
	 * @param processJsonNodeFunct
	 * @throws Exception
	 */
	private static void getNodeValue_internal(final JsonNode node, final String[] names, 
			
			Consumer<String> processStringValueFunct,
			Consumer<Integer> processIntValueFunct,
			Consumer<Double> processDoubbleValueFunct,
			Consumer<String[]> processArrayValueFunct,
			Consumer<JsonNode> processJsonNodeFunct
			) throws Exception {
		
			final JsonNode[] jnode = new JsonNode[] {node};
			Arrays.asList(names).forEach(name -> {
				jnode[0] = jnode[0].get(name);
			});
			
			if (jnode[0].isContainerNode()) {
				processJsonNodeFunct.accept(jnode[0]);
			} else if(jnode[0].isTextual()) {
				processStringValueFunct.accept(jnode[0].asText());
			} else if(jnode[0].isInt()) {
				processIntValueFunct.accept(jnode[0].asInt());
			} else if(jnode[0].isDouble()) {
				processDoubbleValueFunct.accept(jnode[0].asDouble());
			} else if(jnode[0].isFloat()) {
				processDoubbleValueFunct.accept(jnode[0].asDouble());
			}
	}
	
		
	/**
	 * 
	 * @param node
	 * @param name
	 * @return
	 * @throws Exception
	 */
	private static void getNodeValue(JsonNode node, String name,
			
			Consumer<String> processStringValueFunct,
			Consumer<Integer> processIntValueFunct,
			Consumer<Double> processDoubbleValueFunct,
			Consumer<JsonNode> processJsonNodeFunct
			
			) throws Exception {
		JsonNode jnode = node.get(name);
		if (jnode.isContainerNode()) {
			processJsonNodeFunct.accept(jnode);
		} else if(jnode.isTextual()) {
			processStringValueFunct.accept(jnode.asText());
		} else if(jnode.isInt()) {
			processIntValueFunct.accept(jnode.asInt());
		} else if(jnode.isDouble()) {
			processDoubbleValueFunct.accept(jnode.asDouble());
		} else if(jnode.isFloat()) {
			processDoubbleValueFunct.accept(jnode.asDouble());
		}
		
		
	
	}
	
	private static void log(String msg) {
		System.out.println(msg);
	}

}
