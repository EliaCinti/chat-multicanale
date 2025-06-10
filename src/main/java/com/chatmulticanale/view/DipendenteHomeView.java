package com.chatmulticanale.view;

import com.chatmulticanale.controller.InterazioneUtenteController;
import com.chatmulticanale.utils.InputUtils;
import com.chatmulticanale.utils.SessionManager;
import com.chatmulticanale.utils.ViewUtils;
import com.chatmulticanale.view.navigation.Navigazione;
import com.chatmulticanale.view.navigation.View;

/**
 * Rappresenta la schermata principale (Home) per un utente con ruolo Dipendente.
 * Mostra le opzioni disponibili e delega le azioni al controller appropriato.
 */
public class DipendenteHomeView implements View { // <-- Implementa l'interfaccia!

    private final InterazioneUtenteController interazioneController;

    public DipendenteHomeView(InterazioneUtenteController controller) {
        this.interazioneController = controller;
    }

    @Override
    public Navigazione show() {
        // Usiamo un loop infinito perché la navigazione viene gestita tramite 'return'.
        while (true) {
            ViewUtils.printSeparator();
            ViewUtils.println("--- HOME DIPENDENTE ---");
            ViewUtils.println("Benvenuto, " + SessionManager.getInstance().getUtenteLoggato().getNome() + "!");
            ViewUtils.println("\nCosa vuoi fare?");
            ViewUtils.println("1. Visualizza i miei canali");
            ViewUtils.println("2. Invia un messaggio");
            ViewUtils.println("0. Logout");
            ViewUtils.printSeparator();

            int scelta = InputUtils.readInt("Seleziona un'opzione: ");

            switch (scelta) {
                case 1:
                    // TODO: implementare la vista per visualizzare i canali
                    ViewUtils.println("DEBUG: Funzionalità non ancora implementata.");
                    break;
                case 2:
                    // TODO: implementare la vista per inviare un messaggio
                    ViewUtils.println("DEBUG: Funzionalità non ancora implementata.");
                    break;
                case 0:
                    // L'utente vuole fare logout. Restituiamo l'istruzione corretta.
                    SessionManager.getInstance().logout(); // Pulisce la sessione
                    return Navigazione.logout();
                default:
                    ViewUtils.println("Scelta non valida. Riprova.");
            }
        }
    }
}
