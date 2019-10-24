package controlador;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import modelo.Producto;
import util.DateUtil;

import javax.swing.JOptionPane;





public class ProductEditDialogController {
	
	@FXML
    private TextField descripcionField;
    @FXML
    private TextField unidadesField;
    @FXML
    private TextField precioField;
    @FXML
    private TextField fechaField;



    private Stage dialogStage;
    private Producto product;
    private boolean okClicked = false;

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
    }

    /**
     * Sets the stage of this dialog.
     * 
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Sets the person to be edited in the dialog.
     * 
     * @param person
     */
    public void setProduct(Producto product) {
        this.product = product;

        descripcionField.setText(product.getDescripcion());
        unidadesField.setText(Integer.toString(product.getUnidades()));
        precioField.setText(Double.toString(product.getPrecio()));

        fechaField.setText(DateUtil.format(product.getFecha()));
        fechaField.setPromptText("dd.mm.yyyy");
    }

    /**
     * Returns true if the user clicked OK, false otherwise.
     * 
     * @return
     */
    public boolean isOkClicked() {
        return okClicked;
    }

    /**
     * Called when the user clicks ok.
     */
    @FXML
    private void handleOk() {
        if (isInputValid()) {
        	product.setDescripcion(descripcionField.getText());;
        	product.setUnidades(Integer.parseInt(unidadesField.getText()));
        	product.setPrecio(Double.parseDouble(precioField.getText()));
        	product.setFecha(DateUtil.parse(fechaField.getText()));

            okClicked = true;
            dialogStage.close();
        }
    }

    /**
     * Called when the user clicks cancel.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    /**
     * Validates the user input in the text fields.
     * 
     * @return true if the input is valid
     */
    private boolean isInputValid() {
        String errorMessage = "";

        if (descripcionField.getText() == null || descripcionField.getText().length() == 0) {
            errorMessage += "Descripciçon no válida!\n"; 
        }
        if (unidadesField.getText() == null || unidadesField.getText().length() == 0) {
            errorMessage += "Unidades no válida!\n"; 
        }else {
            // try to parse the postal code into an int.
            try {
                Integer.parseInt(unidadesField.getText());
            } catch (NumberFormatException e) {
                errorMessage += "Unidades no válidas (debe ser un entero)!\n"; 
            }
        }
        if (precioField.getText() == null || precioField.getText().length() == 0) {
            errorMessage += "Precio no válido!\n"; 
        }else {
            // try to parse the postal code into an int.
            try {
                Double.parseDouble(precioField.getText());
            } catch (NumberFormatException e) {
                errorMessage += "Precio no válido (debe ser un double)!\n"; 
            }
        }


        if (fechaField.getText() == null || fechaField.getText().length() == 0) {
            errorMessage += "Fecha no válida!\n";
        } else {
            if (!DateUtil.validDate(fechaField.getText())) {
                errorMessage += "Fecha no válida. Use  dd.mm.yyyy!\n";
            }
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
        	  // Show the error message.
            Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Campos erróneos");
            alert.setHeaderText("Por favor corrige los campos erróneos");
            alert.setContentText(errorMessage);
            
            alert.showAndWait();
            
            return false;
        }
    }

}
