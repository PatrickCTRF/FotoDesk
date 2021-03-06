package com.example.patrick.testasdcompermissaoemexecucao;


import android.app.Activity;
import android.content.Intent;
import android.content.UriPermission;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.v4.provider.DocumentFile;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static android.widget.Toast.LENGTH_LONG;

public class OldMain extends AppCompatActivity {

    private static final int REQUEST_WRITE_STORAGE = 112;

    int SELECT_DIR_REQUEST_CODE = 102;
    int SAVE_REQUEST_CODE = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }



    public void saveFile(View view) {
        List<UriPermission> permissions = getContentResolver().getPersistedUriPermissions();
        if (permissions != null && permissions.size() > 0) {
            DocumentFile pickedDir = DocumentFile.fromTreeUri(this, permissions.get(0).getUri());

            Log.v("USAR", pickedDir.findFile("VAISEFUDERVIADO.txt").toString());
            DocumentFile aux = pickedDir.findFile("aaabrao");

            DocumentFile file = pickedDir.createDirectory("aaabrao");
            file.createFile("text/plain", "/aaabrao/try3.txt");
            writeFileContent(file.getUri());



            /*DocumentFile array[] = pickedDir.listFiles();// 430b5a8

            for(DocumentFile i : array){//Imprime mto louco, o toString() de um MESMO objeto MUDA a cada impressão.
                Log.v("cardSD diretorios", "" + i.toString());

                if(i.equals(aux)) Log.v("ENCONTRADO", "Achou");
            }*/


        } else {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TITLE, "TESTEDOCARALHO");

            //startActivityForResult(intent, SAVE_REQUEST_CODE);

            startActivityForResult(new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE), SAVE_REQUEST_CODE);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_DIR_REQUEST_CODE) {
                if (resultData != null) {
                    Uri treeUri=resultData.getData();
                    Log.d("PermissionDemo", "SELECT_DIR_REQUEST_CODE resultData = " + resultData);

                    getContentResolver().takePersistableUriPermission(treeUri, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    DocumentFile pickedDir = DocumentFile.fromTreeUri(getBaseContext(), treeUri);
                    DocumentFile file = pickedDir.createFile("text/plain", "VAISEFUDERVIADO.txt");
                    writeFileContent(file.getUri());



                    //alterDocument(treeUri);
                }
            }
        }
    }

    private void alterDocument(Uri uri) {
        try {
            ParcelFileDescriptor pfd = this.getContentResolver().
                    openFileDescriptor(uri, "w");
            FileOutputStream fileOutputStream =
                    new FileOutputStream(pfd.getFileDescriptor());
            fileOutputStream.write(("Overwritten by MyCloud at " +
                    System.currentTimeMillis() + "\n").getBytes());
            // Let the document provider know you're done by closing the stream.
            fileOutputStream.close();
            pfd.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void writeFileContent(Uri uri) {
        try {
            ParcelFileDescriptor pfd =
                    this.getContentResolver().
                            openFileDescriptor(uri, "w");

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