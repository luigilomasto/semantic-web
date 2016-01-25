import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException; 
import org.jsoup.Jsoup;
import org.w3c.dom.Document;
import org.w3c.tidy.Tidy;

public class HTML_to_XHTML{
   public static void main(String[] args) throws IOException{	  
	         FileInputStream FIS=new FileInputStream("/home/luigi/Scrivania/prova.html");
	         FileOutputStream FOS=new FileOutputStream("/home/luigi/Scrivania/testXHTML.xml");   
	         Tidy t=new Tidy();
	         org.jsoup.nodes.Document doc = Jsoup.connect("http://link.springer.com/article/10.1007%2Fs00236-013-0178-2").get();
	      
	         System.out.println(doc);
	         Document D=t.parseDOM(FIS,FOS);
      }
   }

