package ch.ios.eventapp.service;

import ch.ios.eventapp.domain.Event;
import de.brendamour.jpasskit.PKBarcode;
import de.brendamour.jpasskit.PKField;
import de.brendamour.jpasskit.PKLocation;
import de.brendamour.jpasskit.PKPass;
import de.brendamour.jpasskit.enums.PKBarcodeFormat;
import de.brendamour.jpasskit.passes.PKGenericPass;
import de.brendamour.jpasskit.signing.PKSigningInformation;
import de.brendamour.jpasskit.signing.PKSigningUtil;
import org.apache.commons.compress.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.singletonList;


@Service
public class PassService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);



    public void generatePass(Event event) {
        String appleWWDRCA = "passbook/AppleWWDRCA.pem"; // this is apple's developer relation cert
        String privateKeyPath = "./privateKey.p12"; // the private key you exported from keychain
        String privateKeyPassword = "password"; // the password you used to export
        try {

            PKSigningInformation pkSigningInformation = PKSigningUtil.
                loadSigningInformationFromPKCS12FileAndIntermediateCertificateFile(
                    privateKeyPath, privateKeyPassword, appleWWDRCA);

            PKPass pass = new PKPass();
            pass.setPassTypeIdentifier("pass.com.yourdomain.type");
            pass.setAuthenticationToken("vxwxd7J8AlNNFPS8k0a0FfUFtq0ewzFdc");
            pass.setSerialNumber("12345678000");
            pass.setTeamIdentifier("abcdefg"); // replace this with your team ID
            pass.setOrganizationName("your org");
            pass.setDescription("some description");
            pass.setLogoText("some logo text");

            PKBarcode barcode = new PKBarcode();
            barcode.setFormat(PKBarcodeFormat.PKBarcodeFormatQR);
            barcode.setMessageEncoding(Charset.forName("iso-8859-1"));
            barcode.setMessage("123456789");
            pass.setBarcodes(singletonList(barcode));

            PKGenericPass generic = new PKGenericPass();
            List<PKField> primaryFields = new ArrayList<PKField>();
            PKField member = new PKField();
            member.setKey("mykey"); // some unique key for primary field
            member.setValue("myvalue"); // some value
            primaryFields.add(member);
            generic.setPrimaryFields(primaryFields);
            pass.setGeneric(generic);

            PKLocation location = new PKLocation();
            location.setLatitude(37.33182); // replace with some lat
            location.setLongitude(-122.03118); // replace with some long
            List<PKLocation> locations = new ArrayList<PKLocation>();
            locations.add(location);
            pass.setLocations(locations);

            if (pass.isValid()) {
                String pathToTemplateDirectory = "./mypass.raw"; // replace with your folder with the icons
                byte[] passZipAsByteArray = PKSigningUtil.
                    createSignedAndZippedPkPassArchive(pass, pathToTemplateDirectory, pkSigningInformation);

                String outputFile = "./mypass.pkpass"; // change the name of the pass
                ByteArrayInputStream inputStream = new ByteArrayInputStream(passZipAsByteArray);
                IOUtils.copy(inputStream, new FileOutputStream(outputFile));
                System.out.println("Done!");
            } else {
                System.out.println("the pass is NOT Valid man!!!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("failed!");
        }
    }
}
