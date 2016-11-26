package yourpackage;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 *
 * @author Emil
 * @param <T>
 */
public class MyFile<T>{
    
    private String fileName;
    private File f;
    
    public MyFile(){
        fileName = "MyFile.dat";
        f = new File(fileName);
    }
    public MyFile(String fileName){
        this.fileName = fileName;
        f = new File(fileName);
    }
    @Override
    public String toString(){
        return fileName;
    }
    public long getTotalSpace(){
        return f.getTotalSpace();
    }
    public T upload() {
        try(ObjectInputStream IN = new ObjectInputStream(new FileInputStream(fileName))){
                return (T) IN.readObject();
        } catch (IOException | ClassNotFoundException | NullPointerException ex) {
            return null;
        }       
    }
    public T[] upload(T[] e){
        ArrayList<T> a = new ArrayList();
        T t = null;
        int i=0;
        try(ObjectInputStream IN = new ObjectInputStream(new FileInputStream(fileName))){
            while(true){   
                a.add((T) IN.readObject());
                i++; 
            }
        }
        catch(EOFException ex){
            t = a.get(0);
            return a.toArray((T[])Array.newInstance(t.getClass(), i));
        }
        catch (IOException | ClassNotFoundException | NullPointerException ex) {
            return null;
        }       
    }
    public T[] upload(int sizeArray){
        ArrayList<T> a = new ArrayList();
        try(ObjectInputStream IN = new ObjectInputStream(new FileInputStream(fileName))){
            int i=0;
            while(i<sizeArray){
                a.add((T)IN.readObject());
                i++;
            }
            T t = a.get(0);
            return a.toArray((T[])Array.newInstance(t.getClass(), sizeArray));
        }
        catch (IOException | ClassNotFoundException | NullPointerException ex) {
            return null;
        }       
    }
    public boolean download(T e, boolean append){
        if(!f.exists() || !append){     
            try (ObjectOutputStream OUT = new ObjectOutputStream(new FileOutputStream(fileName))) {
                OUT.writeObject(e);
                return true;
            } catch (IOException ex) {
                return false;
            }
        }
        else {
            try (AppendingObjectOutputStream OUT = new AppendingObjectOutputStream(new FileOutputStream(fileName, append))) {
                    OUT.writeObject(e);
                    return true;
            } catch (IOException ex) {
                return false;
            }
        }
    }
    public boolean download(T[] e, boolean append){
        if(!f.exists() || !append){
            try (ObjectOutputStream OUT = new ObjectOutputStream(new FileOutputStream(fileName))) {
                int i=0;
                while(i<e.length){
                    OUT.writeObject(e[i]);
                    i++;
                }
                return true;
            } catch (IOException ex) {
                return false;
            }
        }
        else {
            try (AppendingObjectOutputStream OUT = new AppendingObjectOutputStream(new FileOutputStream(fileName, append))) {
                int i=0;
                while(i<e.length){
                    OUT.writeObject(e[i]);
                    i++;
                }
                return true;
            } catch (IOException ex) {
                return false;
            }
        }
    }
    public boolean download(T[] e, int size, boolean append){
        if(!f.exists() || !append){
            try (ObjectOutputStream OUT = new ObjectOutputStream(new FileOutputStream(fileName, append))) {
                int i=0;
                while(i<size && i<e.length){
                    OUT.writeObject(e[i]);
                    i++;
                }
                return true;
            } catch (IOException | NullPointerException ex) {
                return false;
            }
        }
        else{
            try (AppendingObjectOutputStream OUT = new AppendingObjectOutputStream(new FileOutputStream(fileName, append))) {
                int i=0;
                while(i<size && i<e.length){
                    OUT.writeObject(e[i]);
                    i++;
                }
                return true;
            } catch (IOException ex) {
                return false;
            }
        }
    }
    private class AppendingObjectOutputStream extends ObjectOutputStream{
    
        protected AppendingObjectOutputStream(OutputStream out) throws IOException{
            super(out);
        }

        @Override
        protected void writeStreamHeader() throws IOException {
        // do not write a header, but reset:
            reset();
        }

    }
}
