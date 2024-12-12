package gov.cdc.dataingestion.commands;

import gov.cdc.dataingestion.model.AuthModel;
import gov.cdc.dataingestion.util.AuthUtil;
import gov.cdc.dataingestion.util.PropUtil;
import io.github.pixee.security.BoundedLineReader;
import picocli.CommandLine;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@CommandLine.Command(name = "validatehl7", mixinStandardHelpOptions = true, description = "This functionality will validate the provided HL7 message.")
public class Hl7Validation extends PropUtil implements Runnable{
    @CommandLine.Option(names = {"--hl7-file"}, description = "HL7 file name with fully qualified path", interactive = true, echo = true, required = true)
    String hl7FilePath;

    AuthModel authModel = new AuthModel();
    AuthUtil authUtil = new AuthUtil();

    @Override
    @SuppressWarnings("java:S106")
    public void run() {
        if(hl7FilePath != null) {
                StringBuilder requestBody = new StringBuilder();
                try(BufferedReader reader = new BufferedReader(new FileReader(hl7FilePath))) {
                    String line;
                    while((line = BoundedLineReader.readLine(reader, 5_000_000)) != null) {
                        requestBody.append(line + "\n");
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                // Serving data from INT1 environment as the production doesn't have data yet
                authModel.setServiceEndpoint(getProperty("service.env.url") + getProperty("service.env.hl7Validation"));
                authModel.setRequestBody(requestBody.toString());

                String apiResponse = authUtil.getResponseFromDIService(authModel, "hl7validation");
                System.out.println(apiResponse);
            }
        else {
            System.err.println("HL7 file path is null.");
        }
    }
}
