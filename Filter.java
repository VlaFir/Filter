
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Filter {
    public static void main(String[] args) throws IOException {
        String fileName = "in1.txt";
        Scanner scanner = new Scanner(new File(fileName), StandardCharsets.UTF_8 );
        while(scanner.hasNextLine()){
            String line = (String) scanner.nextLine();
            System.out.println(line);
            System.out.println("Строка является: " + lineType(line));
            
        }
        scanner.close();
        
        // try ( FileReader fileReader = new FileReader(new File(fileName), StandardCharsets.UTF_8)) {
        //     StringBuilder stringB = new StringBuilder();
        //     int count = 0;
        //     while(fileReader.ready()){
        //         char charset = (char) fileReader.read();
        //         stringB.append(charset);
        //         if(charset == '\n'){
        //             System.out.println(stringB + "  - является: " + lineType(stringB.toString()));
        //             System.out.println("Найден перенос");
        //             stringB.delete(0, count--);
        //         }

        //         count++;
        //     }
            
        // }
    }

    public static String lineType (String input) {
        // Проверка на целое число (INTEGER)
        if (input.matches("^-?\\d+$")) {
            return "INTEGER";
        }

        // Проверка на дробное число (FLOAT или DOUBLE)
        else if (input.matches("^-?\\d+\\.\\d+$")) {
            return "FLOAT";
        }
        else{
            // Если не подходит ни под один шаблон, считаем обычной строкой (STRING)
            return "STRING";
        }
        
        
    }
    
}
