import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class Emas {
    public static void main(String[] args) throws FileNotFoundException {
        Gson gson = new Gson();
        DataModel data = gson.fromJson(new FileReader("imperative/data.json"), DataModel.class);
        System.out.println(data);
    }
}
