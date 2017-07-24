package com.example.patrick.testasdcompermissaoemexecucao;


import android.app.Activity;
import android.content.Intent;
import android.content.UriPermission;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.v4.provider.DocumentFile;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    int SAVE_REQUEST_CODE = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    public void saveFile(View view) {
        List<UriPermission> permissions = getContentResolver().getPersistedUriPermissions();
        if (permissions != null && permissions.size() > 0) {

            //pickedDir is the directory the user selected (I chose SD's root).
            DocumentFile pickedDir = DocumentFile.fromTreeUri(this, permissions.get(0).getUri());



//=====You can use these lines to write into an existent folder or directory========================
            DocumentFile aux = pickedDir.findFile("aaabrao");//aaabrao is the name of a existing folder in my SD.

    //Use DocumentFile file = pickedDir.createDirectory("aaabrao"); to create a new folder in the pickedDir directory.

            if(aux != null) {
                //Creating the object "file" is essencial for you to write "some text" INSIDE the TXT file. Otherwise, your TXT will be a blank.
                DocumentFile file = aux.createFile("text/plain", "try5.txt");
                writeFileContent(file.getUri());
            }
//==================================================================================================



//=====You can use these lines to write to the primary chosen directory (SD's root, in my case)====
            DocumentFile file2 = pickedDir.createFile("text/plain", "try5.txt");
            writeFileContent(file2.getUri());
//==================================================================================================

        } else {

            startActivityForResult(new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE), SAVE_REQUEST_CODE);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SAVE_REQUEST_CODE) {
                if (resultData != null) {
                    Uri treeUri=resultData.getData();

                    //This line gets your persistent permission to write SD (or anywhere else) without prompting a request everytime.
                    getContentResolver().takePersistableUriPermission(treeUri, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                    //These lines will write to the choosen directory (SD's root was my chosen one). You could also write to an existent folder inside SD's root like me in saveFile().
                    DocumentFile pickedDir = DocumentFile.fromTreeUri(getBaseContext(), treeUri);
                    DocumentFile file = pickedDir.createFile("text/plain", "try5.txt");
                    writeFileContent(file.getUri());

                }
            }
        }
    }


    private void writeFileContent(Uri uri) {
        try {
            ParcelFileDescriptor pfd = this.getContentResolver().openFileDescriptor(uri, "w");

            FileOutputStream fileOutputStream = new FileOutputStream(pfd.getFileDescriptor());

            String textContent = "some text";

            fileOutputStream.write(textContent.getBytes());

            fileOutputStream.close();
            pfd.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}