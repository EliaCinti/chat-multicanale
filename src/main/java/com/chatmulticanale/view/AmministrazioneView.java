package com.chatmulticanale.view;

import com.chatmulticanale.controller.AmministrazioneController;
import com.chatmulticanale.dto.ProgettoResponsabileDTO;
import com.chatmulticanale.exception.CommandException;
import com.chatmulticanale.model.Progetto;
import com.chatmulticanale.model.Utente;
import com.chatmulticanale.utils.*;
import com.chatmulticanale.view.navigation.Navigazione;
import com.chatmulticanale.view.navigation.View;
import java.util.List;

/**
 * Rappresenta la schermata principale (Home) per un utente con ruolo Amministratore.
 * Fornisce accesso a tutte le funzionalità di gestione del sistema.
 */
public class AmministrazioneView implements View {
    private final AmministrazioneController adminController;

    public AmministrazioneView(AmministrazioneController controller) {
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
            ViewUtils.println("5. Aggiungi progetto");
            ViewUtils.println("0. Logout");
            ViewUtils.printSeparator();

            int scelta = InputUtils.readIntInRange("Seleziona un'opzione: ", 0, 5);
            switch (scelta) {
                case 1:
                    handlePromuoviUtente();
                    break;
                case 2:
                    handleDegradaCapoProgetto();
                    break;
                case 3:
                    handleAssegnaResponsabilita();
                    break;
                case 4:
                    handleRiassegnaProgetto();
                    break;
                case 5:
                    handleAggiungiProgetto();
                    break;
                case 0:
                    SessionManager.getInstance().logout();
                    return Navigazione.logout();
            }
        }
    }

    private void handlePromuoviUtente() {
        ViewUtils.clearScreen();
        ViewUtils.println(ColorUtils.ANSI_BOLD + "--- PROMUOVI UTENTE A CAPO PROGETTO (AM1) ---" + ColorUtils.ANSI_RESET);
        ViewUtils.println("Digita '/b' o '/back' per annullare.");
        ViewUtils.printSeparator();

        List<Utente> dipendenti = adminController.getListaUtentiPromuovibili();

        if (dipendenti.isEmpty()) {
            ViewUtils.println("Nessun dipendente da promuovere al momento.");
        } else {
            try {
                ViewUtils.println("Lista dei Dipendenti che possono essere promossi:");
                for (Utente utente : dipendenti) {
                    String rigaUtente = String.format("ID: %-5d | Nome: %-15s | Cognome: %s",
                            utente.getIdUtente(),
                            utente.getNome(),
                            utente.getCognome());
                    ViewUtils.println(rigaUtente);
                }
                ViewUtils.printSeparator();

                int idUtenteDaPromuovere = InputHelper.chiediIdValido("Inserisci l'ID dell'utente da promuovere: ", dipendenti);

                // Adesso siamo sicuri al 100% che 'idUtenteDaPromuovere' è un ID valido e presente nella lista.
                boolean successo = adminController.promuoviUtenteACapoProgetto(idUtenteDaPromuovere);
                if (successo) {
                    ViewUtils.println(ColorUtils.ANSI_GREEN + "\nUtente promosso con successo!" + ColorUtils.ANSI_RESET);
                } else {
                    ViewUtils.println(ColorUtils.ANSI_RED + "\nERRORE: Impossibile promuovere l'utente. Controllare i log." + ColorUtils.ANSI_RESET);
                }

            } catch (CommandException e) {
                // Gestisce il comando /b o /back
                ViewUtils.println("\nOperazione annullata.");
            }
        }
        InputUtils.pressEnterToContinue("Premi Invio per tornare al menu...");
    }

    /**
     * Gestisce il flusso guidato e sicuro per la rimozione del ruolo di Capo Progetto,
     * forzando la riassegnazione delle responsabilità come da regola aziendale RA3.
     */
    private void handleDegradaCapoProgetto() {
        ViewUtils.clearScreen();
        ViewUtils.println(ColorUtils.ANSI_BOLD + "--- RIMUOVI RUOLO CAPO PROGETTO (Procedura Guidata) ---" + ColorUtils.ANSI_RESET);
        ViewUtils.println("Digita '/b' o '/back' per annullare in qualsiasi momento.");
        ViewUtils.printSeparator();

        // 1. Chiedi quale Capo Progetto degradare
        List<Utente> capiProgetto = adminController.getListaCapiProgetto();
        if (capiProgetto.isEmpty()) {
            ViewUtils.println("Nessun Capo Progetto da rimuovere.");
            InputUtils.pressEnterToContinue("\nPremi Invio per tornare al menu...");
            return;
        }

        // Aggiungiamo il blocco try-catch per gestire la CommandException di InputUtils
        try {
            ViewUtils.println("Lista dei Capi Progetto attuali:");
            capiProgetto.forEach(u -> ViewUtils.println(String.format("ID: %-5d | Nome: %-15s | Cognome: %s", u.getIdUtente(), u.getNome(), u.getCognome())));
            ViewUtils.printSeparator();

            int idDaDegradare = InputHelper.chiediIdValido("Inserisci l'ID del Capo Progetto da degradare: ", capiProgetto);

            final int finalIdDaDegradare = idDaDegradare; // Necessario per la lambda
            List<Utente> altriCapi = capiProgetto.stream()
                    .filter(cp -> cp.getIdUtente() != finalIdDaDegradare)
                    .toList();

            // Questo controllo ora è ancora più importante
            if (altriCapi.isEmpty() && !adminController.getProgettiDiUnResponsabile(idDaDegradare).isEmpty()) {
                ViewUtils.println(ColorUtils.ANSI_RED + "\nERRORE: Impossibile procedere. Questo utente ha progetti da riassegnare, ma non ci sono altri Capi Progetto disponibili." + ColorUtils.ANSI_RESET);
                InputUtils.pressEnterToContinue("\nPremi Invio per tornare al menu...");
                return;
            }

            // 3. Trova e gestisci i progetti dell'utente da degradare
            List<Progetto> progettiDaRiassegnare = adminController.getProgettiDiUnResponsabile(idDaDegradare);
            if (!progettiDaRiassegnare.isEmpty()) {
                ViewUtils.println(ColorUtils.ANSI_YELLOW + "\nATTENZIONE: Questo utente è responsabile dei seguenti progetti." + ColorUtils.ANSI_RESET);
                ViewUtils.println("È necessario riassegnarli tutti prima di poter procedere con il degrado.");

                for (Progetto progetto : progettiDaRiassegnare) {
                    ViewUtils.printSeparator();
                    ViewUtils.println("Riassegnazione del progetto: '" + progetto.getNomeProgetto() + "' (ID: " + progetto.getIdProgetto() + ")");
                    ViewUtils.println("\nCapi Progetto disponibili per la riassegnazione:");
                    altriCapi.forEach(u -> ViewUtils.println(String.format("ID: %-5d | Nome: %-15s", u.getIdUtente(), u.getNome())));

                    int idNuovoResponsabile = InputHelper.chiediIdValido("\nInserisci l'ID del nuovo responsabile per questo progetto: ", altriCapi);

                    if (adminController.riassegnaProgetto(progetto.getIdProgetto(), idNuovoResponsabile)) {
                        ViewUtils.println(ColorUtils.ANSI_GREEN + "Progetto riassegnato con successo." + ColorUtils.ANSI_RESET);
                    } else {
                        ViewUtils.println(ColorUtils.ANSI_RED + "ERRORE durante la riassegnazione. Procedura annullata." + ColorUtils.ANSI_RESET);
                        InputUtils.pressEnterToContinue("");
                        return; // Interrompe l'intera operazione se una riassegnazione fallisce
                    }
                }
                ViewUtils.println(ColorUtils.ANSI_GREEN + "\nTutte le responsabilità sono state trasferite con successo." + ColorUtils.ANSI_RESET);
            } else {
                ViewUtils.println("\nL'utente selezionato non ha responsabilità di progetto attive.");
            }

            // 4. Esegui il degrado finale
            ViewUtils.println("Procedo con il degrado del ruolo...");
            if (adminController.finalizzaDegradoCapoProgetto(idDaDegradare)) {
                ViewUtils.println(ColorUtils.ANSI_GREEN + "Ruolo rimosso con successo! L'utente è ora un Dipendente." + ColorUtils.ANSI_RESET);
            } else {
                ViewUtils.println(ColorUtils.ANSI_RED + "ERRORE finale durante il degrado del ruolo." + ColorUtils.ANSI_RESET);
            }

        } catch (CommandException e) {
            // Se l'utente digita /b o /back in qualsiasi momento, l'operazione viene annullata.
            ViewUtils.println("\nOperazione annullata.");
        }
        InputUtils.pressEnterToContinue("Premi Invio per tornare al menu...");
    }

    /**
     * Gestisce il flusso per l'aggiunta di un nuovo progetto.
     */
    private void handleAggiungiProgetto() {
        ViewUtils.clearScreen();
        ViewUtils.println(ColorUtils.ANSI_BOLD + "--- AGGIUNGI PROGETTO ---" + ColorUtils.ANSI_RESET);
        ViewUtils.println("Digita '/b' o '/back' per annullare.");
        ViewUtils.printSeparator();

        try {
            String nomeProgetto = InputUtils.askForInput("Nome del nuovo progetto: ");
            String descrizioneProgetto = InputUtils.askForInput("Descrizione del progetto: ");

            if (adminController.creaNuovoProgetto(nomeProgetto, descrizioneProgetto)) {
                ViewUtils.println(ColorUtils.ANSI_GREEN + "\nProgetto '" + nomeProgetto + "' creato con successo!" + ColorUtils.ANSI_RESET);
                ViewUtils.println("Ora puoi assegnarlo a un Capo Progetto usando l'opzione 3.");
            } else {
                ViewUtils.println(ColorUtils.ANSI_RED + "\nERRORE: Impossibile creare il progetto. Il nome potrebbe essere già esistente." + ColorUtils.ANSI_RESET);
            }
        } catch (CommandException e) {
            ViewUtils.println("\nOperazione annullata.");
        }

        InputUtils.pressEnterToContinue("Premi Invio per tornare al menu...");
    }

    /**
     * Gestisce il flusso per assegnare un progetto non assegnato a un Capo Progetto (AM3).
     * Riutilizza il metodo helper 'chiediIdValido' per una validazione dell'input robusta.
     */
    private void handleAssegnaResponsabilita() {
        ViewUtils.clearScreen();
        ViewUtils.println(ColorUtils.ANSI_BOLD + "--- ASSEGNA RESPONSABILITÀ PROGETTO (AM3) ---" + ColorUtils.ANSI_RESET);
        ViewUtils.println("Digita '/b' o '/back' per annullare in qualsiasi momento.");
        ViewUtils.printSeparator();

        // 1. Recupera i dati necessari
        List<Progetto> progetti = adminController.getListaProgettiNonAssegnati();
        if (progetti.isEmpty()) {
            ViewUtils.println("Nessun progetto da assegnare al momento.");
            InputUtils.pressEnterToContinue("\nPremi Invio per tornare al menu...");
            return;
        }

        List<Utente> capiProgetto = adminController.getListaCapiProgetto();
        if (capiProgetto.isEmpty()) {
            ViewUtils.println("Nessun Capo Progetto disponibile a cui assegnare progetti.");
            InputUtils.pressEnterToContinue("\nPremi Invio per tornare al menu...");
            return;
        }

        try {
            // 2. Mostra i progetti e chiede una selezione VALIDA
            ViewUtils.println("Lista dei Progetti non assegnati:");
            progetti.forEach(p -> ViewUtils.println(String.format("ID: %-5d | Nome: %s", p.getIdProgetto(), p.getNomeProgetto())));
            ViewUtils.printSeparator();
            int idProgetto = InputHelper.chiediIdValido("Inserisci l'ID del progetto da assegnare: ", progetti);


            // 3. Mostra i capi progetto e chiede una selezione VALIDA
            ViewUtils.println("\nLista dei Capi Progetto disponibili:");
            capiProgetto.forEach(cp -> ViewUtils.println(String.format("ID: %-5d | Nome: %s %s", cp.getIdUtente(), cp.getNome(), cp.getCognome())));
            ViewUtils.printSeparator();
            int idCapo = InputHelper.chiediIdValido("Inserisci l'ID del Capo Progetto a cui assegnare il progetto: ", capiProgetto);

            // 4. Esegue l'azione solo dopo aver ricevuto input validati
            if (adminController.assegnaProgetto(idProgetto, idCapo)) {
                ViewUtils.println(ColorUtils.ANSI_GREEN + "\nProgetto assegnato con successo!" + ColorUtils.ANSI_RESET);
            } else {
                ViewUtils.println(ColorUtils.ANSI_RED + "\nERRORE: Impossibile assegnare il progetto. Controllare i log." + ColorUtils.ANSI_RESET);
            }

        } catch (CommandException e) {
            ViewUtils.println("\nOperazione annullata.");
        }

        InputUtils.pressEnterToContinue("Premi Invio per tornare al menu...");
    }

    /**
     * Gestisce il flusso guidato per riassegnare un progetto da un Capo Progetto a un altro (AM4).
     * Esclude il responsabile attuale dalla lista dei nuovi candidati.
     */
    private void handleRiassegnaProgetto() {
        ViewUtils.clearScreen();
        ViewUtils.println(ColorUtils.ANSI_BOLD + "--- RIASSEGNA RESPONSABILITÀ PROGETTO (AM4) ---" + ColorUtils.ANSI_RESET);
        ViewUtils.println("Digita '/b' o '/back' per annullare in qualsiasi momento.");
        ViewUtils.printSeparator();

        // 1. Recupera i dati necessari
        List<ProgettoResponsabileDTO> progettiAssegnati = adminController.getListaProgettiConResponsabile();
        if (progettiAssegnati.isEmpty()) {
            ViewUtils.println("Nessun progetto attualmente assegnato da poter riassegnare.");
            InputUtils.pressEnterToContinue("\nPremi Invio per tornare al menu...");
            return;
        }

        List<Utente> tuttiCapiProgetto = adminController.getListaCapiProgetto();
        // Se c'è meno di un altro capo progetto, la riassegnazione è impossibile.
        if (tuttiCapiProgetto.size() < 2) {
            ViewUtils.println(ColorUtils.ANSI_YELLOW + "Attenzione: Non è possibile effettuare riassegnazioni perché non ci sono abbastanza Capi Progetto." + ColorUtils.ANSI_RESET);
            InputUtils.pressEnterToContinue("\nPremi Invio per tornare al menu...");
            return;
        }

        try {
            // 2. Mostra i progetti e chiede una selezione valida
            ViewUtils.println("Lista dei Progetti attualmente assegnati:");
            progettiAssegnati.forEach(dto -> {
                String riga = String.format("ID: %-5d | Nome: %-25s | Responsabile: %s %s",
                        dto.getIdProgetto(), dto.getNomeProgetto(), dto.getNomeResponsabile(), dto.getCognomeResponsabile());
                ViewUtils.println(riga);
            });
            ViewUtils.printSeparator();

            int idProgetto = InputHelper.chiediIdValido("Inserisci l'ID del progetto da riassegnare: ", progettiAssegnati);

            // 3. Identifica il responsabile attuale e filtra la lista dei candidati
            final int idResponsabileAttuale = progettiAssegnati.stream()
                    .filter(p -> p.getIdProgetto() == idProgetto)
                    .findFirst()
                    .map(ProgettoResponsabileDTO::getIdResponsabile) // Grazie al DTO modificato!
                    .orElseThrow(() -> new IllegalStateException("Logica inconsistente: progetto non trovato dopo validazione."));

            List<Utente> capiProgettoDisponibili = tuttiCapiProgetto.stream()
                    .filter(cp -> cp.getIdUtente() != idResponsabileAttuale)
                    .toList();

            if (capiProgettoDisponibili.isEmpty()) {
                ViewUtils.println(ColorUtils.ANSI_RED + "\nERRORE: Non ci sono altri Capi Progetto disponibili a cui riassegnare questo progetto." + ColorUtils.ANSI_RESET);
                InputUtils.pressEnterToContinue("\nPremi Invio per tornare al menu...");
                return;
            }

            // 4. Mostra la lista filtrata e chiede la nuova selezione
            ViewUtils.println("\nLista dei Capi Progetto disponibili (escluso il responsabile attuale):");
            capiProgettoDisponibili.forEach(cp -> ViewUtils.println(String.format("ID: %-5d | Nome: %s %s", cp.getIdUtente(), cp.getNome(), cp.getCognome())));
            ViewUtils.printSeparator();

            // La validazione avviene sulla lista dei candidati disponibili
            int idNuovoCapo = InputHelper.chiediIdValido("Inserisci l'ID del NUOVO Capo Progetto responsabile: ", capiProgettoDisponibili);

            // 5. Esegue l'azione
            if (adminController.riassegnaProgetto(idProgetto, idNuovoCapo)) {
                ViewUtils.println(ColorUtils.ANSI_GREEN + "\nProgetto riassegnato con successo!" + ColorUtils.ANSI_RESET);
            } else {
                ViewUtils.println(ColorUtils.ANSI_RED + "\nERRORE: Impossibile riassegnare il progetto. Controllare i log." + ColorUtils.ANSI_RESET);
            }

        } catch (CommandException e) {
            ViewUtils.println("\nOperazione annullata.");
        }

        InputUtils.pressEnterToContinue("Premi Invio per tornare al menu...");
    }
}
