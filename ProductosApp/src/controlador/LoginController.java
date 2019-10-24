package controlador;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.prefs.Preferences;
import util.LocalDateAdapter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.controlsfx.dialog.Dialogs;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modelo.Producto;

public class LoginController {
	private Stage stage;
	
	//Lista de productos
    private ObservableList<Producto> productData=FXCollections.observableArrayList();
    
    //Constructor
    public LoginController() {
    	productData.add(new Producto("Super Mario Maker 2",2,59.95,LocalDate.of(2019, 6, 30)));
    	productData.add(new Producto("Zelda: Link´s Awakening",10,54.95,LocalDate.of(2019,9, 21)));
    	productData.add(new Producto("OverWatch",5,44.95,LocalDate.of(2019, 10, 4)));
    	productData.add(new Producto("Mario Kart Deluxe",8,49.95,LocalDate.of(2018, 12, 25)));
    	productData.add(new Producto("Donkey Kong Country",14,29.95,LocalDate.of(2018, 05, 30)));
    	productData.add(new Producto("Crash Bandicoot",3,44.95,LocalDate.of(2019, 8, 20)));
    }
    
    public ObservableList<Producto> getProductData() {
        return productData;
    }
    
		@FXML
	    private Button btnLogin;

	    @FXML
	    private TextField txtEmail;

	    @FXML
	    private TextField txtPassword;
	    
	    @FXML
	    void login(ActionEvent event) throws IOException {
	    	//Creamos la alerta que aparecerá para decirnos si hemos introducido bien las credenciales
	    	Alert alert = new Alert(AlertType.INFORMATION);
	    	alert.setTitle("Información");
	    	alert.setHeaderText(null);
	    	
	    	//Variables ficticias para emular un inicio de sesión
	    	String email = "prueba@gmail.com";
	    	String password = "123456";
	    	
	    	
	    	//Comprobamos si coinciden los datos del login
	    	if(txtEmail.getText().equals(email) && txtPassword.getText().equals(password)) {
	    		//Si coincide, nos muestra el siguiente mensaje
	    		alert.setContentText("Ha iniciado sesión correctamente");  
	    		
	    		cambiarVentana(event);
	    	}else {
	    		//Si no hemos escrito bien nuestra credenciales, nos muestra este otro
	    		alert.setContentText("Email y/o contraseña incorrectos");
	    		System.out.println(email +" "+password+" "+txtEmail.getText()+" "+txtPassword.getText()+" ");
	    	}
	    	
	    	alert.showAndWait();
	    }
	    public Stage getPrimaryStage() {
	        return stage;
	    }
	    
	    private void cambiarVentana(ActionEvent event) throws IOException {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../vista/ProductOverview.fxml"));
			BorderPane admin = (BorderPane)loader.load();
			
	    	Scene adminScene = new Scene(admin,800,500);
	    	stage = (Stage)((Node)event.getSource()).getScene().getWindow();
	    	stage.setTitle("Lista productos");
	    	
	    	this.stage.getIcons().add(new Image("file:recursos/imagenes/iconfinder_videogame_icons-01_611922"));
	    	stage.setScene(adminScene);
	    	stage.show();
	    	
	    	//Acceso a la app principal
	    	ProductOverviewController prodCont = loader.getController();
	    	prodCont.setMainApp(this);
	    	
	        // Try to load last opened person file.
	        File file = getProductFilePath();
	        if (file != null) {
	            loadProductDataFromFile(file);
	        }
	    }
	    
	    public boolean showProductEditDialog(Producto product) {
	        try {
	            // Load the fxml file and create a new stage for the popup dialog.
	            FXMLLoader loader = new FXMLLoader();
	            loader.setLocation(LoginController.class.getResource("../vista/ProductEditDialog.fxml"));
	            AnchorPane page = (AnchorPane) loader.load();

	            // Create the dialog Stage.
	            Stage dialogStage = new Stage();
	            dialogStage.setTitle("Editar Producto");
	            dialogStage.initModality(Modality.WINDOW_MODAL);
	            dialogStage.initOwner(stage);
	            Scene scene = new Scene(page);
	            dialogStage.setScene(scene);

	            // Set the product into the controller.
	            ProductEditDialogController controller = loader.getController();
	            controller.setDialogStage(dialogStage);
	            controller.setProduct(product);

	            // Show the dialog and wait until the user closes it
	            dialogStage.showAndWait();

	            return controller.isOkClicked();
	        } catch (IOException e) {
	            e.printStackTrace();
	            return false;
	        }
	    }
	    
	    /**
	     * Returns the person file preference, i.e. the file that was last opened.
	     * The preference is read from the OS specific registry. If no such
	     * preference can be found, null is returned.
	     * 
	     * @return
	     */
	    public File getProductFilePath() {
	        Preferences prefs = Preferences.userNodeForPackage(LoginController.class);
	        String filePath = prefs.get("filePath", null);
	        if (filePath != null) {
	            return new File(filePath);
	        } else {
	            return null;
	        }
	    }

	    /**
	     * Sets the file path of the currently loaded file. The path is persisted in
	     * the OS specific registry.
	     * 
	     * @param file the file or null to remove the path
	     */
	    public void setProductFilePath(File file) {
	        Preferences prefs = Preferences.userNodeForPackage(LoginController.class);
	        if (file != null) {
	            prefs.put("filePath", file.getPath());

	            // Update the stage title.
	            stage.setTitle("ProductApp - " + file.getName());
	        } else {
	            prefs.remove("filePath");

	            // Update the stage title.
	            stage.setTitle("ProductApp");
	        }
	    }
	    
	    public void loadProductDataFromFile(File file) {
	        try {
	            JAXBContext context = JAXBContext
	                    .newInstance(ProductListWrapper.class);
	            Unmarshaller um = context.createUnmarshaller();

	            // Reading XML from the file and unmarshalling.
	            ProductListWrapper wrapper = (ProductListWrapper) um.unmarshal(file);

	            productData.clear();
	            productData.addAll(wrapper.getProducts());

	            // Save the file path to the registry.
	            setProductFilePath(file);

	        } catch (Exception e) { // catches ANY exception
	            Alert alert = new Alert(AlertType.ERROR);
	            alert.setTitle("Error");
	            alert.setHeaderText("No se puedieron cargar los datos");
	            alert.setContentText("No se pudieron cargar los datos del archivo:\n" + file.getPath());

	            alert.showAndWait();
	        }
	    }
	    
	    public void saveProductDataToFile(File file) {
	        try {
	            JAXBContext context = JAXBContext
	                    .newInstance(ProductListWrapper.class);
	            Marshaller m = context.createMarshaller();
	            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

	            // Wrapping our person data.
	            ProductListWrapper wrapper = new ProductListWrapper();
	            wrapper.setProducts(productData);

	            // Marshalling and saving XML to the file.
	            m.marshal(wrapper, file);

	            // Save the file path to the registry.
	            setProductFilePath(file);
	        } catch (Exception e) { // catches ANY exception
	            Alert alert = new Alert(AlertType.ERROR);
	            alert.setTitle("Error");
	            alert.setHeaderText("No se pudieron salvar los datos");
	            alert.setContentText("No se pudieron salvar en el archivo:\n" + file.getPath());

	            alert.showAndWait();
	        }
	    }
	    
	    
}
