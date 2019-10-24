package controlador;

import java.io.File;

import javax.swing.JOptionPane;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import modelo.Producto;
import util.DateUtil;

public class ProductOverviewController {
		
		@FXML
	    private TableView<Producto> productTable;
	    @FXML
	    private TableColumn<Producto, String> descripcionColumn;
	    @FXML
	    private TableColumn<Producto, Integer> unidadesColumn;

	    @FXML
	    private Label descripcionLabel;
	    @FXML
	    private Label unidadesLabel;
	    @FXML
	    private Label precioLabel;
	    @FXML
	    private Label fechaLabel;


	    // Reference to the main application.
	    private LoginController login;

	    /**
	     * The constructor.
	     * The constructor is called before the initialize() method.
	     */
	    public ProductOverviewController() {
	    }

	    /**
	     * Initializes the controller class. This method is automatically called
	     * after the fxml file has been loaded.
	     */
	  
	    @FXML
	    private void initialize() {
	        // Initialize the person table with the two columns.
	    	
	    	descripcionColumn.setCellValueFactory(cellData -> cellData.getValue().descripcionProperty());
	        unidadesColumn.setCellValueFactory(cellData -> cellData.getValue().unidadesProperty().asObject());
	        
	     // Clear person details.
	        showProductDetails(null);

	        // Listen for selection changes and show the person details when changed.
	        productTable.getSelectionModel().selectedItemProperty().addListener(
	                (observable, oldValue, newValue) -> showProductDetails(newValue));
	    }


	    public void setMainApp(LoginController login) {
	        this.login = login;

	        // Add observable list data to the table
	        productTable.setItems(login.getProductData());
	    }
	    
	    
	    private void showProductDetails(Producto product) {
	        if (product != null) {
	            // Fill the labels with info from the person object.
	            descripcionLabel.setText(product.getDescripcion());
	            unidadesLabel.setText(Integer.toString(product.getUnidades()));
	            precioLabel.setText(Double.toString(product.getPrecio()));
	            fechaLabel.setText(DateUtil.format(product.getFecha()));
	            
	            // TODO: We need a way to convert the birthday into a String! 
	            // birthdayLabel.setText(...);
	        } else {
	            // Person is null, remove all the text.
	        	descripcionLabel.setText("");
	            unidadesLabel.setText("");
	            precioLabel.setText("");		            
	            fechaLabel.setText("");
	        }
	    }
	    
	    /**
	     * Called when the user clicks on the delete button.
	     */
	    @FXML
	    private void handleDeleteProduct() {
	        int selectedIndex = productTable.getSelectionModel().getSelectedIndex();
	        
	        if(selectedIndex>=0)
	        {
	        	productTable.getItems().remove(selectedIndex);
	        }else {
	            // Nothing selected.
	        	JOptionPane.showMessageDialog(null, "No ha seleccionado ningún producto", null, 0);
	        }
	    }
	    
	    /**
	    * Called when the user clicks the new button. Opens a dialog to edit
	    * details for a new person.
	    */
	   @FXML
	   private void handleNewProduct() {
	       Producto tempProduct = new Producto();
	       boolean okClicked = login.showProductEditDialog(tempProduct);
	       if (okClicked) {
	           login.getProductData().add(tempProduct);
	       }
	   }

	   /**
	    * Called when the user clicks the edit button. Opens a dialog to edit
	    * details for the selected product.
	    */
	   @FXML
	   private void handleEditProduct() {
	       Producto selectedProduct = productTable.getSelectionModel().getSelectedItem();
	       if (selectedProduct != null) {
	           boolean okClicked = login.showProductEditDialog(selectedProduct);
	           if (okClicked) {
	               showProductDetails(selectedProduct);
	           }

	       } else {
	           Alert alert = new Alert(AlertType.WARNING);
	           alert.initOwner(login.getPrimaryStage());
	           alert.setTitle("Ninguna selección");
	           alert.setHeaderText("Ningún producto seleccionador");
	           alert.setContentText("Por favor seleccione un producto en la tabla.");

	           alert.showAndWait();
	       }
	   }
	   
	   @FXML
	    private void handleNew() {
	        login.getProductData().clear();
	        login.setProductFilePath(null);
	    }

	    /**
	     * Opens a FileChooser to let the user select an address book to load.
	     */
	    @FXML
	    private void handleOpen() {
	        FileChooser fileChooser = new FileChooser();

	        // Set extension filter
	        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
	                "XML files (*.xml)", "*.xml");
	        fileChooser.getExtensionFilters().add(extFilter);

	        // Show save file dialog
	        File file = fileChooser.showOpenDialog(login.getPrimaryStage());

	        if (file != null) {
	            login.loadProductDataFromFile(file);
	        }
	    }

	    /**
	     * Saves the file to the person file that is currently open. If there is no
	     * open file, the "save as" dialog is shown.
	     */
	    @FXML
	    private void handleSave() {
	        File personFile = login.getProductFilePath();
	        if (personFile != null) {
	            login.saveProductDataToFile(personFile);
	        } else {
	            handleSaveAs();
	        }
	    }

	    /**
	     * Opens a FileChooser to let the user select a file to save to.
	     */
	    @FXML
	    private void handleSaveAs() {
	        FileChooser fileChooser = new FileChooser();

	        // Set extension filter
	        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
	                "XML files (*.xml)", "*.xml");
	        fileChooser.getExtensionFilters().add(extFilter);

	        // Show save file dialog
	        File file = fileChooser.showSaveDialog(login.getPrimaryStage());

	        if (file != null) {
	            // Make sure it has the correct extension
	            if (!file.getPath().endsWith(".xml")) {
	                file = new File(file.getPath() + ".xml");
	            }
	            login.saveProductDataToFile(file);
	        }
	    }

	    /**
	     * Opens an about dialog.
	     */
	    @FXML
	    private void handleAbout() {
	        Alert alert = new Alert(AlertType.INFORMATION);
	        alert.setTitle("ProductApp");
	        alert.setHeaderText("About");
	        alert.setContentText("Author: Joaquín Corugedo");

	        alert.showAndWait();
	    }

	    /**
	     * Closes the application.
	     */
	    @FXML
	    private void handleExit() {
	        System.exit(0);
	    }
}
