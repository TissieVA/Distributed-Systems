package be.ua.fti.ei.client;

import be.ua.fti.ei.utils.http.FileBody;
import be.ua.fti.ei.utils.http.HttpRequester;
import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.*;

public class DirectoryUpdateCheck extends Thread
{
    private final WatchService watchService;
    private WatchKey key;
    private static Gson gson;

    public DirectoryUpdateCheck(Path path) throws IOException
    {
        this.gson = new Gson();
        this.watchService = FileSystems.getDefault().newWatchService();
        path.register(
                watchService,
                StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_DELETE,
                StandardWatchEventKinds.ENTRY_MODIFY);
    }

    public void run()
    {
        while (true)
        {
            try
            {
                if ((key = watchService.take()) == null) break;
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            for(WatchEvent<?> event : key.pollEvents())
            {

                if (event.kind().equals(StandardWatchEventKinds.ENTRY_CREATE))
                {
                    System.out.println("File added");
                    String filename = event.context().toString();
                    if (!(filename.contains(".swp") || filename.startsWith(".")))
                    {
                        FileBody fb = new FileBody(filename, Node.getClient().getNodeBody());
                        HttpRequester.POST(Node.getClient().getNameServerAddress() + "/files/add", gson.toJson(fb));
                    }
                }
                else if (event.kind().equals(StandardWatchEventKinds.ENTRY_DELETE))
                {
                    System.out.println("File Deleted");
                    String filename = event.context().toString();
                    if (!(filename.contains(".swp") || filename.startsWith(".")))
                    {
                        FileBody fb = new FileBody(filename, Node.getClient().getNodeBody());
                        HttpRequester.POST(Node.getClient().getNameServerAddress() + "/files/delete", gson.toJson(fb));
                    }
                }
                else if (event.kind().equals(StandardWatchEventKinds.ENTRY_MODIFY))
                {
                    System.out.println("File modified");
                    String filename = event.context().toString();
                    if (!(filename.contains(".swp") || filename.startsWith(".")))
                    {
                        FileBody fb = new FileBody(filename, Node.getClient().getNodeBody());
                        HttpRequester.POST(Node.getClient().getNameServerAddress() + "/files/add", gson.toJson(fb));
                    }
                }
            }
            key.reset();
        }
    }
}
