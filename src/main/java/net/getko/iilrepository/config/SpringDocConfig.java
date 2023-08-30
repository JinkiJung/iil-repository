
package net.getko.iilrepository.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * The SpringFoxConfig Class.
 * <p>
 * This configuration controls the swagger behaviour.
 *
 * @author Jinki Jung (email: your.jinki.jung@gmail.com)
 */
@Configuration
public class SpringDocConfig {

    @Value("${swagger.title:iil Repository API}" )
    private String swaggerTitle;

    @Value("${swagger.description:Backend for iil data storage}" )
    private String swaggerDescription;

    @Value("${swagger.version:0.0}" )
    private String swaggerVersion;

    @Value("${swagger.termsOfServiceUrl:null}" )
    private String swaggerTermsOfServiceUrl;

    @Value("${swagger.contactName:}" )
    private String swaggerContactName;

    @Value("${swagger.contactUrl:}" )
    private String swaggerContactUrl;

    @Value("${swagger.contactEmail:}" )
    private String swaggerContactEmail;

    @Value("${swagger.licence:MIT license}" )
    private String swaggerLicence;

    @Value("${swagger.licenceUrl:https://www.mit.edu/~amini/LICENSE.md}" )
    private String swaggerLicenceUrl;

    /**
     * API docket.
     *
     * @return the API docket
     */
    @Bean
    @Qualifier("openApi")
    @Primary
    public OpenAPI openApi() {
        return new OpenAPI()
                .info(this.apiInfo());
    }

    /**
     * Returns the main API Information.
     *
     * @return the API information object
     */
    private Info apiInfo() {
        // Create the contact info
        Contact contact = new Contact();
        contact.setName(this.swaggerContactName);
        contact.setUrl(this.swaggerContactUrl);
        contact.setEmail(this.swaggerContactEmail);

        //Create the licence
        License license = new License();
        license.setName(this.swaggerLicence);
        license.setUrl(this.swaggerLicenceUrl);

        // And return the API info
        return new Info()
                .title(this.swaggerTitle)
                .description(this.swaggerDescription)
                .version(this.swaggerVersion)
                .termsOfService(this.swaggerTermsOfServiceUrl)
                .contact(contact)
                .license(license);
    }

}
