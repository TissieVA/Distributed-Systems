package be.ua.fti.ei.client;

import be.ua.fti.ei.utils.http.FileBody;
import be.ua.fti.ei.utils.http.HttpRequester;
import be.ua.fti.ei.utils.http.NodeBody;
import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

public class DirectoryUpdateCheck
{
    WatchService watchService;
    Path path;
    WatchKey key;
    private static Gson gson;

    public DirectoryUpdateCheck(Path path) throws IOException
    {
        this.watchService = FileSystems.getDefault().newWatchService();
        this.path = path;
        this.path.register(
                watchService,
                StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_DELETE,
                StandardWatchEventKinds.ENTRY_MODIFY);
    }

    public void start() throws InterruptedException
    {
        while ((key = watchService.take()) != null)
        {
            for(WatchEvent<?> event : key.pollEvents())
            {

                if (event.kind().equals(StandardWatchEventKinds.ENTRY_CREATE))
                {
                    System.out.println("File added");
                    FileBody fb = new FileBody(event.context().toString(),Node.getClient().getNodeBody());
                    HttpRequester.POST(Node.getClient().getNameServerAddress() + "/files/add", gson.toJson(fb));
                }
                else if (event.kind().equals(StandardWatchEventKinds.ENTRY_DELETE))
                {
                    System.out.println("File Deleted");
                }
                else if (event.kind().equals(StandardWatchEventKinds.ENTRY_MODIFY))
                {
                    System.out.println("File modified");
                    FileBody fb = new FileBody(event.context().toString(),Node.getClient().getNodeBody());
                    HttpRequester.POST(Node.getClient().getNameServerAddress() + "/files/add", gson.toJson(fb));
                }
            }
            key.reset();
        }
    }
}
