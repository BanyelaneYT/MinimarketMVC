/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 * DocumentFilter que solo permite letras (mayúsculas/minúsculas) y espacios.
 */
class LetterOnlyDocumentFilter extends DocumentFilter {

    private static final String LETTER_REGEX = "^[a-zA-Z\\s]*$";

    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
        if (string == null) {
            return;
        }

        // Verifica si la cadena a insertar solo contiene letras y espacios
        if (string.matches(LETTER_REGEX)) {
            super.insertString(fb, offset, string, attr);
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        if (text == null) {
            super.replace(fb, offset, length, text, attrs);
            return;
        }

        // Construye la cadena resultante
        String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
        String beforeOffset = currentText.substring(0, offset);
        String afterOffset = currentText.substring(offset + length);
        String newText = beforeOffset + text + afterOffset;

        // Verifica si la cadena resultante solo contiene letras y espacios
        if (newText.matches(LETTER_REGEX)) {
            super.replace(fb, offset, length, text, attrs);
        }
    }
}