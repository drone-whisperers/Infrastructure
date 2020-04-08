import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.io.PrintWriter;
import java.net.ServerSocket;

public class SimpleFileClient {

  public final static int SOCKET_PORT = 16667;      // you may change this
  public final static String SERVER = "module2";  // localhost
  public final static String
       FILE_TO_RECEIVED = "/resources/audio_files";  // you may change this, I give a
                                                            // different name because i don't want to
                                                            // overwrite the one used by server...

  public final static int FILE_SIZE = 6022386; // file size temporary hard coded
                                               // should bigger than the file to be downloaded
  public final static int fileNum = 0;      // you may change this

  public static void main (String [] args ) throws IOException {

    final File folder = new File(FILE_TO_RECEIVED);

    List<String> result = new ArrayList<>();

    search(".*\\.wav", folder, result);
    int current = 0;
    int fileNum = 1;
    ServerSocket listener = new ServerSocket(11111);
    System.out.println("module 1 listening for incoming commands");
    while(true){
      if(fileNum >= result.size()){
        System.out.println("No more audio files to process shutting down");
      }
      Socket socket = listener.accept();
      PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
      FileInputStream fis = null;
      BufferedInputStream bis = null;
      Socket sock = null;
      try {
      sock = new Socket(SERVER, SOCKET_PORT);
      System.out.println("command received processing to wav then sending");

      // receive file
      byte [] buff  = new byte [8192];
      OutputStream os = sock.getOutputStream();
      fis = new FileInputStream("/resources/audio_files/Audio"+fileNum+".wav");
      bis = new BufferedInputStream(fis);

      int count;

      while((count = bis.read(buff)) > 0){
        os.write(buff, 0, count);
      }
        System.out.println("File " + FILE_TO_RECEIVED + " downloaded (" + current + " bytes read)");
      }
      finally {
        if (fis != null) fis.close();
        if (bis != null) bis.close();
        if (sock != null) sock.close();
        }
      fileNum++;
    }
  }

  public static void search(final String pattern, final File folder, List<String> result) {
    for (final File f : folder.listFiles()) {

        if (f.isDirectory()) {
            search(pattern, f, result);
        }

        if (f.isFile()) {
            if (f.getName().matches(pattern)) {
                result.add(f.getAbsolutePath());
            }
        }

    }
}

}