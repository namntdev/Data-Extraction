package net.nguyenthanhnam.api;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import vn.pipeline.Annotation;
import vn.pipeline.Sentence;
import vn.pipeline.VnCoreNLP;
import vn.pipeline.Word;

@RestController
public class ExtractionController {
	@RequestMapping("/parse")
	public ArrayList<Item> parse(@RequestParam(value = "content", defaultValue = "World") String content) {
		String[] annotators = { "wseg", "pos", "ner", "parse" };
		ArrayList<Word> words = new ArrayList<Word>();
		try {
			VnCoreNLP pipeline = new VnCoreNLP(annotators);
//		        String str = "Ông Nguyễn Khắc Chúc  đang làm việc tại Đại học Quốc gia Hà Nội. "
//                + "Bà Lan, vợ ông Chúc, cũng làm việc tại đây."; 
			String str = content; //"Tôi cần mua bảo hiểm một xe Kia Morning và hai xe Mazda CX5.";
			Annotation annotation = new Annotation(str);
			pipeline.annotate(annotation);

			System.out.println(annotation.toString());
			// 1 Ông Nc O 4 sub
			// 2 Nguyễn_Khắc_Chúc Np B-PER 1 nmod
			// 3 đang R O 4 adv
			// 4 làm_việc V O 0 root
			// ...

			// Write to file
			// PrintStream outputPrinter = new PrintStream("output.txt");
			// pipeline.printToFile(annotation, outputPrinter);

			// You can also get a single sentence to analyze individually
			Sentence firstSentence = annotation.getSentences().get(0);
			words = (ArrayList) firstSentence.getWords();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		
		ArrayList<String> amountList = new ArrayList<String>();
		ArrayList<String> nameList = new ArrayList<String>();
		for (Word word : words) {
			System.out.println(word.getDepLabel());
			System.out.println(word.getForm());
			System.out.println(word.getNerLabel());
			System.out.println(word.getPosTag());
			if (word.getPosTag().equalsIgnoreCase("M")) {
				amountList.add(word.getForm());
			}
			if (word.getPosTag().equalsIgnoreCase("Np")) {
				nameList.add(word.getForm());
			}
		}
		ArrayList<Item> items = new ArrayList<Item>();
		for (int i = 0; i < nameList.size(); i++) {
			Item itemTmp = new Item();
			int amount = 0;
			try {
				amount = Integer.parseInt(amountList.get(i));
			} catch (Exception e) {
				// TODO: handle exception
				amount = convertQuantity(amountList.get(i));
				System.out.println(amount);
			}

			itemTmp.setAmount(amount);
			itemTmp.setName(nameList.get(i));
			items.add(itemTmp);
		}

		return items;
	}
	
	@RequestMapping("/test")
	public Item test() {
		// Create a file object 
        File f = new File("program.txt"); 

        // Get the absolute path of file f 
        String absolute = f.getAbsolutePath(); 

        // Display the file path of the file object 
        // and also the file path of absolute file 
        System.out.println("Original  path: "
                           + f.getPath()); 
        System.out.println("Absolute  path: "
                           + absolute); 
		return new Item(1, "Hello world");
	}
	
	public int convertQuantity(String quantity) {
		int res = 0;
		String quantityList[] = { "không", "một", "hai", "ba", "bốn", "năm", "sáu", "bảy", "tám", "chín", "mười" };
		for (int i = 0; i < quantityList.length; i++) {
			if (quantityList[i].equalsIgnoreCase(quantity)) {
				res = i;
				break;
			}
		}

		return res;
	}

}
