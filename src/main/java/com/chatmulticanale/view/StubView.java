package com.chatmulticanale.view;

import com.chatmulticanale.utils.InputUtils;
import com.chatmulticanale.utils.ViewUtils;
import com.chatmulticanale.view.navigation.Navigazione;
import com.chatmulticanale.view.navigation.View;

/**
 * Una vista "segnaposto" (stub).
 * Il suo unico scopo è mostrare un messaggio che informa l'utente
 * che una funzionalità non è ancora stata implementata, e poi tornare indietro.
 * È utile per evitare errori di compilazione e per costruire l'applicazione
 * in modo incrementale.
 */
public class StubView implements View {

    private final String message;

    /**
     * Costruttore che accetta il messaggio da visualizzare.
     * @param message Il testo da mostrare all'utente (es. "Funzionalità non implementata.").
     */
    public StubView(String message) {
        this.message = message;
    }

    public StubView() {
        this.message = "Funzionalità non ancora implementata.";
    }

    /**
     * Mostra il messaggio e attende la pressione di Invio per tornare indietro.
     * @return Un'istruzione Navigazione.indietro() per tornare alla vista precedente.
     */
    @Override
    public Navigazione show() {
        ViewUtils.printSeparator();
        ViewUtils.println(this.message); // Mostra il messaggio personalizzato
        ViewUtils.printSeparator();

        InputUtils.pressEnterToContinue("Premi Invio per tornare indietro...");

        // Dice al sistema di navigazione di tornare alla schermata precedente.
        return Navigazione.indietro();
    }
}