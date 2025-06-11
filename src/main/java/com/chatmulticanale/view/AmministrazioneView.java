package com.chatmulticanale.view;

import com.chatmulticanale.controller.AmministrazioneController;
import com.chatmulticanale.model.Utente;
import com.chatmulticanale.utils.ColorUtils;
import com.chatmulticanale.utils.InputUtils;
import com.chatmulticanale.utils.SessionManager;
import com.chatmulticanale.utils.ViewUtils;
import com.chatmulticanale.view.navigation.Navigazione;
import com.chatmulticanale.view.navigation.View;

import java.util.List;

/**
 * Rappresenta la schermata principale (Home) per un utente con ruolo Amministratore.
 * Fornisce accesso a tutte le funzionalità di gestione del sistema.
 */
public class AmministrazioneView implements View { // <-- NOME CLASSE CORRETTO
    private final AmministrazioneController adminController;

    /**
     * Costruttore della AmministrazioneView.
     * @param controller Il controller che gestisce le azioni amministrative.
     */
    public AmministrazioneView(AmministrazioneController controller) { // <-- NOME COSTRUTTORE CORRETTO
        this.adminController = controller;
    }

    @Override
    public Navigazione show() {
        while (true) {
            ViewUtils.clearScreen();
            ViewUtils.println(ColorUtils.ANSI_BOLD + "--- HOME AMMINISTRATORE ---" + ColorUtils.ANSI_RESET);
            ViewUtils.println("Benvenuto, " + SessionManager.getInstance().getUtenteLoggato().getUsername() + "!");
            ViewUtils.printSeparator();
            ViewUtils.println("1. Promuovi un utente a Capo Progetto (AM1)");
            ViewUtils.println("2. Rimuovi ruolo Capo Progetto (AM2)");
            ViewUtils.println("3. Assegna responsabilità Progetto a un Capo Progetto (AM3)");
            ViewUtils.println("4. Riassegna responsabilità Progetto (AM4)");
            ViewUtils.println("0. Logout");
            ViewUtils.printSeparator();

            int scelta = InputUtils.readInt("Seleziona un'opzione: ");
            switch (scelta) {
                case 1:
                    handlePromuoviUtente();
                    break;
                case 2, 3, 4:
                    new StubView("Funzionalità non ancora implementata.").show();
                    break;
                case 0:
                    SessionManager.getInstance().logout();
                    return Navigazione.logout();
                default:
                    ViewUtils.println(ColorUtils.ANSI_RED + "Scelta non valida. Riprova." + ColorUtils.ANSI_RESET);
                    InputUtils.pressEnterToContinue("");
            }
        }
    }

    private void handlePromuoviUtente() {
        ViewUtils.clearScreen();
        ViewUtils.println(ColorUtils.ANSI_BOLD + "--- PROMUOVI UTENTE A CAPO PROGETTO ---" + ColorUtils.ANSI_RESET);

        List<Utente> dipendenti = adminController.getListaUtentiPromuovibili();

        if (dipendenti.isEmpty()) {
            ViewUtils.println("Nessun dipendente da promuovere al momento.");
        } else {
            ViewUtils.println("");
            ViewUtils.printSeparator2();
            ViewUtils.println("Lista dei Dipendenti che possono essere promossi:");
            ViewUtils.printSeparator2();
            ViewUtils.println("");

            for (Utente utente : dipendenti) {
                String rigaUtente = String.format("ID: %-5d | Nome: %-15s | Cognome: %s",
                        utente.getIdUtente(),
                        utente.getNome(),
                        utente.getCognome());
                ViewUtils.println(rigaUtente);
            }
            ViewUtils.printSeparator();

            int idUtente = InputUtils.readInt("Inserisci l'ID dell'utente da promuovere (o '0' per annullare): ");

            if (idUtente != 0) {
                boolean successo = adminController.promuoviUtenteACapoProgetto(idUtente);
                if (successo) {
                    ViewUtils.println(ColorUtils.ANSI_GREEN + "\nUtente promosso con successo!" + ColorUtils.ANSI_RESET);
                } else {
                    ViewUtils.println(ColorUtils.ANSI_RED + "\nERRORE: Impossibile promuovere l'utente." + ColorUtils.ANSI_RESET);
                }
            } else {
                ViewUtils.println("Operazione annullata.");
            }
        }

        InputUtils.pressEnterToContinue("\nPremi Invio per tornare al menu...");
    }
}