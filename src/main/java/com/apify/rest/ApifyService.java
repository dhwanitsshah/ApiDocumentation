package com.apify.rest;

import java.io.File;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.fop.afp.modca.triplets.MeasurementUnitsTriplet;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import com.apify.model.ApiSpec;
import com.apify.model.MethodArgument;
import com.apify.rest.ApifyDocHelper;

@Path("/apidoc")
public class ApifyService extends Application {

	/*@GET
	@Path("/get")
	@Produces("application/pdf")
	public Response getFile() {
		String path = System.getProperty("user.dir").toString();
		File file = new File(path);
		ResponseBuilder response = Response.ok((Object) file);
		response.header("Content-Disposition", "attachment; filename=new-android-book.pdf");
		return response.build();
	}*/

	@POST
	@Path("/post")
	@Consumes("application/json")
	public Response consumeJSON(ApiSpec asp) {
		String output = asp.toString();
		return Response.status(200).entity(output).build();
	}

	@GET
	@Path("/getFunctionDoc")
	@Produces("application/json")
	public Response getSampleApiJSON() throws Exception {
		
		MethodArgument inputArg1 = new MethodArgument("firstName", "String","First name of the user");
		MethodArgument inputArg2 = new MethodArgument("lastName", "String","Last name of the user");
		MethodArgument outputArg = new MethodArgument(null, "String","Greeting message from the system");
		MethodArgument inputArgs[] = {inputArg1, inputArg2};
		
		ApiSpec asp = new ApiSpec("com.amex.sample", "print", "public", inputArgs, outputArg,
				"It will print hello world with given firstname and lastname");

		//This will create a word document
		WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage();
		ApifyDocHelper.createStyledDocumentFromApiSpec(wordMLPackage, asp);
		
		// Now save it at the following location
		System.out.println(System.getProperty("user.dir"));
		
		wordMLPackage.save(new java.io.File(System.getProperty("user.dir") + "/ApiDoc.doc"));
		return Response.status(200).entity(asp).build();
	}
	
	@GET
	@Path("/getApiDoc")
	@Produces("application/doc")
	public Response downloadApiDocument() {
		String path = System.getProperty("user.dir").toString();
		path+="//Api_Documentation.doc";
		File file = new File(path);
		ResponseBuilder response = Response.ok((Object) file);
		response.header("Content-Disposition", "attachment; filename=ApiDoc.doc");
		return response.build();
	}
	
	@GET
	@Path("/apify")
	@Produces("application/json")
	public Response apify() throws Exception {
		
		ApiSpec asp = makeApifyDocument("{apiUrl}", "{apiName}", "{methodType}", "{requestPayload}", "{responsePayload}", "{contentType}");
		return Response.status(200).entity(asp).build();
	}
	
	public ApiSpec makeApifyDocument(String apiUrl, String apiName, String methodType, String requestPayload, String responsePayload, String contentType) throws Exception {
		
		ApiSpec asp = new ApiSpec(apiUrl, apiName, methodType, requestPayload, responsePayload, contentType);
		
		//This will create a word document
		WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage();
		ApifyDocHelper.createStyledDocumentFromApiSpec(wordMLPackage, asp);
		
		// Now save it at the following location
		System.out.println(System.getProperty("user.dir"));
		wordMLPackage.save(new java.io.File(System.getProperty("user.dir") + "//ApiDoc.doc"));
		
		return asp;
	}

	
	
}