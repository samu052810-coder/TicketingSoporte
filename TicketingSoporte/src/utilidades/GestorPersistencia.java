package utilidades;

import java.io.*;

public class GestorPersistencia {
    
    /**
     * Guarda cualquier objeto que implemente Serializable en un archivo.
     */
    public static void guardarObjeto(Object objeto, String rutaArchivo) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(rutaArchivo);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(objeto);
        }
    }

    /**
     * Carga un objeto desde un archivo.
     */
    public static Object cargarObjeto(String rutaArchivo) throws IOException, ClassNotFoundException {
        try (FileInputStream fis = new FileInputStream(rutaArchivo);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return ois.readObject();
        }
    }
}
