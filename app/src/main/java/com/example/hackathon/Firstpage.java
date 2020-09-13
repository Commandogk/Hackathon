package com.example.hackathon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Firstpage extends AppCompatActivity  {
    ImageButton add, search, refresh, sort, paste,back;
    private String[] Permission = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private int STORAGE_PERMISSION_CODE = 1;
    String path = Environment.getExternalStorageDirectory().getAbsolutePath();
    boolean sortorder , nameenter = false,bck = false;
    private static final int REQUEST_CODE = 2;
    String foldername = null;
    //   String sortmode = "Name";
    itemclick itemclick = new itemclick() {
        @Override
        public void onitemClick(file file, int position) {
            itempop(file,position);
        }
    };
     String mode = null;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstpage);
        add = findViewById(R.id.bt1);
        search = findViewById(R.id.bt2);
        refresh = findViewById(R.id.bt3);
        sort = findViewById(R.id.bt4);
        back = findViewById(R.id.backdirectory);
        paste=findViewById(R.id.bt5);
        recyclerView = findViewById(R.id.list);
        paste.setEnabled(false);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            sort.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sortpop();
                }
            });

            refresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    function();
                }
            });
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popbox();
                }
            });
            bck=false;
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bck = true;
                    function();
                }
            });
            function();

        }
    }

    private void sortpop() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sort By");

        final View customLayout = getLayoutInflater().inflate(R.layout.sortpop, null);
        builder.setView(customLayout);
        builder.setPositiveButton("Ascending",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int w) {
                        RadioGroup rGroup = (RadioGroup) customLayout.findViewById(R.id.radioGroup1);
                        RadioButton checkedRadioButton = (RadioButton) rGroup.findViewById(rGroup.getCheckedRadioButtonId());
                        //  sortmode = checkedRadioButton.getText().toString();
                        sortorder = false;
                        if (sortorder)
                            Toast.makeText(getApplicationContext(), "descending order", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(getApplicationContext(), "ascending order", Toast.LENGTH_SHORT).show();
                        function();
                    }
                });
        builder.setNegativeButton("Descending", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                RadioGroup rGroup = (RadioGroup) customLayout.findViewById(R.id.radioGroup1);
                RadioButton checkedRadioButton = (RadioButton) rGroup.findViewById(rGroup.getCheckedRadioButtonId());
                //  sortmode = checkedRadioButton.getText().toString();
                sortorder = true;
                if (sortorder)
                    Toast.makeText(getApplicationContext(), "descending order", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), "ascending order", Toast.LENGTH_SHORT).show();
                function();
            }
        });
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void popbox() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("File Name");

        final View customLayout = getLayoutInflater().inflate(R.layout.addfilepop, null);
        builder.setView(customLayout);
        builder.setPositiveButton("Enter",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int w) {
                        nameenter = true;
                        EditText editText = customLayout.findViewById(R.id.filename);
                        if (editText.getText().toString().isEmpty()) {
                            Toast.makeText(getApplicationContext(), "no name entered", Toast.LENGTH_SHORT).show();
                        } else {
                            foldername = editText.getText().toString();
                            File f = new File(path, foldername);
                            if (!f.exists()) {
                                f.mkdirs();
                                Toast.makeText(getApplicationContext(), foldername + "\t created"+"\n"+path, Toast.LENGTH_SHORT).show();
                                function();
                            }
                        }

                    }
                });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                nameenter = false;
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void function() {
        TextView textView = findViewById(R.id.directory);
        ArrayList<file> filedetails = new ArrayList<>();
        ArrayList<String> filename = new ArrayList<>();
        File root;
        Intent a = getIntent();
        path = a.getStringExtra("path");
        if (path == null || path.isEmpty()) {
            path = Environment.getExternalStorageDirectory().getAbsolutePath();
            root = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        } else {
            root = new File(path);
        }
        if(bck){
            int p2 = path.lastIndexOf("/");
            path = path.substring(0,p2);
            a.putExtra("path",path);
            root = new File(path);
            bck=false;
        }
        String name = root.getAbsolutePath().toString();
        int pos = name.lastIndexOf("/");
        name = name.substring(pos + 1);
        if (name.equals("0"))
            name = "Internal Storage";
        textView.setText(name);
        File[] files = root.listFiles();
        filedetails.clear();
        filename.clear();
        if (files != null) {
            for (File file1 : files) {
                file file = new file();
                file.setFilename(file1.getName());
                file.setFilepath(file1.getAbsolutePath());
                file.setFilesize(String.valueOf(file1.getTotalSpace()));
                filename.add(file.getFilename());
                filedetails.add(file);
            }
            filedetails = sortlist(filename, sortorder, filedetails);
            recycleradapter myAdapter1;
            myAdapter1 = new recycleradapter(getApplicationContext(), filedetails,itemclick);
            recyclerView.setAdapter(myAdapter1);
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        }
    }

    private ArrayList<file> sortlist(ArrayList<String> filename, boolean sortorder, ArrayList<file> filedetails) {
        ArrayList<file> files = filedetails;
        if (!sortorder) {
            Collections.sort(filename);
            for (int i = 0; i < files.size(); i++) {
                String filename1 = filename.get(i);
                for (int j = 0; j < files.size(); j++) {
                    if (files.get(j).getFilename().equals(filename1)) {
                        file tmp = new file();
                        file tmp1 = new file();
                        tmp = files.get(j);
                        tmp1 = files.get(i);
                        files.set(i, tmp);
                        files.set(j, tmp1);
                    }
                }
            }
            return files;

        } else {
            Collections.sort(filename, Collections.reverseOrder());
            for (int i = 0; i < files.size(); i++) {
                String filename1 = filename.get(i);
                for (int j = 0; j < files.size(); j++) {
                    if (files.get(j).getFilename().equals(filename1)) {
                        file tmp = new file();
                        file tmp1 = new file();
                        tmp = files.get(j);
                        tmp1 = files.get(i);
                        files.set(i, tmp);
                        files.set(j, tmp1);
                    }
                }
            }
            for(int i =0;i<files.size();i++){
                Toast.makeText(getApplicationContext(),""+filename.get(i)+"\n"+files.get(i).getFilename(),Toast.LENGTH_SHORT).show();
            }
            return files;
        }
    }


    private void itempop(final file itemfile, int itemp) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("perform");
        final View customLayout = getLayoutInflater().inflate(R.layout.funtions, null);
        builder.setView(customLayout);
        builder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int w) {
                        RadioGroup rGroup = (RadioGroup) customLayout.findViewById(R.id.radioGroup2);
                        RadioButton checkedRadioButton = (RadioButton) rGroup.findViewById(rGroup.getCheckedRadioButtonId());
                        mode = checkedRadioButton.getText().toString();
                        if(mode.equals("delete")){
                            delete(itemfile.filepath);
                            function();
                        }
                        else if(mode.equals("move")){
                            move(itemfile);
                            function();
                        }
                        else  if(mode.equals("copy"))
                            copy(itemfile);
                        Toast.makeText(getApplicationContext(), ""+mode, Toast.LENGTH_SHORT).show();
                    }
                });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                function();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void copy(file itemfile) {
        final   file copy= itemfile;
        paste.setEnabled(true);
        paste.setEnabled(true);
        paste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),path+"\n"+copy.filepath,Toast.LENGTH_SHORT).show();
                File o = new File(copy.filepath, copy.filename);
                File c = new File(path, copy.filename);
                pasting(o,c);
            }
        });
    }

    private void move(file itemfile) {
         final   file copy= itemfile;
        paste.setEnabled(true);
        Toast.makeText(getApplicationContext(),"paste"+"\n"+path,Toast.LENGTH_SHORT).show();
       paste.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               File o = new File(copy.filepath, copy.filename);
               File c = new File(path, copy.filename);
               pasting(o,c);

           }
       });


    }

    private void pasting(File o, File c) {
        if(!o.isDirectory()){
            try {
                InputStream inputStream = new FileInputStream(o);
                OutputStream outputStream = new FileOutputStream(c);
                byte[] buf = new byte[1024];
                int len;
                while ((len = inputStream.read(buf)) >= 0) {
                    outputStream.write(buf, 0, len);
                }
                outputStream.close();
                inputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            File[] f = o.listFiles();
            assert f != null;
            final File newFolder = new File(c.getParent() + "/" + o.getAbsolutePath().substring(o.getAbsolutePath().lastIndexOf("/") + 1));
            Toast.makeText(getApplicationContext(),c.getParent() + "/" + o.getAbsolutePath().substring(o.getAbsolutePath().lastIndexOf("/") + 1),Toast.LENGTH_SHORT).show();
            if (!newFolder.exists()) {
                newFolder.mkdir();
                Toast.makeText(getApplicationContext(),"created",Toast.LENGTH_SHORT).show();
                function();
            }
            Log.d("checking", o.getAbsolutePath() + "  :copy:  " + newFolder.getAbsolutePath());
            for(File file : f) {
                File newFile = new File(newFolder.getAbsolutePath() + file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf('/')));
                Log.d("checking", file.getAbsolutePath()  + "  :copy:  " + newFile.getAbsolutePath());
                pasting(file, newFile);
            }
        }
        }

    private void delete(String p) {
        File file = new File(p);
        if(file.exists()) {
            if (file.isDirectory()) {
                File[] children = file.listFiles();
                for (File child : children) {
                    if(child.isDirectory())
                        delete(child.getAbsolutePath());
                    else
                        child.delete();
                }
                file.delete();
            } else {
                file.delete();
            }
        }
    }
}
