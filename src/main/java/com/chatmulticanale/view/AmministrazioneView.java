package com.chatmulticanale.view;

import com.chatmulticanale.controller.AmministrazioneController;
import com.chatmulticanale.dto.ProgettoResponsabileDTO;
import com.chatmulticanale.exception.CommandException;
import com.chatmulticanale.model.Progetto;
import com.chatmulticanale.model.Utente;
import com.chatmulticanale.utils.*;
import com.chatmulticanale.view.costanti_view.CostantiView;
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
            ViewUtils.println(ColorUtils.ANSI_BOLD + CostantiView.HOME_AMMINISTRATORE + ColorUtils.ANSI_RESET);
            ViewUtils.println(CostantiView.WELCOME + SessionManager.getInstance().getUtenteLoggato().getUsername() + "!");
            ViewUtils.printSeparator();
            ViewUtils.println(CostantiView.HOME_AMMINISTRATORE_OPZIONE_1);
            ViewUtils.println(CostantiView.HOME_AMMINISTRATORE_OPZIONE_2);
            ViewUtils.println(CostantiView.HOME_AMMINISTRATORE_OPZIONE_3);
            ViewUtils.println(CostantiView.HOME_AMMINISTRATORE_OPZIONE_4);
            ViewUtils.println(CostantiView.HOME_AMMINISTRATORE_OPZIONE_5);
            ViewUtils.println(CostantiView.HOME_AMMINISTRATORE_OPZIONE_0);
            ViewUtils.printSeparator();

            int scelta = InputUtils.readIntInRange(CostantiView.SELEZIONA_OPZIONE, 0, 5);
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
        ViewUtils.println(ColorUtils.ANSI_BOLD + CostantiView.PROMUOVI_UTENTE + ColorUtils.ANSI_RESET);
        ViewUtils.println(CostantiView.B_O_BACK);
        ViewUtils.printSeparator();

        List<Utente> dipendenti = adminController.getListaUtentiPromuovibili();

        if (dipendenti.isEmpty()) {
            ViewUtils.println(CostantiView.PROMUOVI_UTENTE_NO_DIPENDENTI);
        } else {
            try {
                ViewUtils.println(CostantiView.PROMUOVI_UTENTE_LISTA_DIPENDENTI);
                for (Utente utente : dipendenti) {
                    String rigaUtente = String.format(CostantiView.FORMATO_NOME_COMPLETO_UTENTE,
                            utente.getIdUtente(),
                            utente.getNome(),
                            utente.getCognome());
                    ViewUtils.println(rigaUtente);
                }
                ViewUtils.printSeparator();

                int idUtenteDaPromuovere = InputHelper.chiediIdValido(CostantiView.PROMUOVI_UTENTE_ID_UTENTE, dipendenti);

                // Adesso siamo sicuri al 100% che 'idUtenteDaPromuovere' è un ID valido e presente nella lista.
                boolean successo = adminController.promuoviUtenteACapoProgetto(idUtenteDaPromuovere);
                if (successo) {
                    ViewUtils.println(ColorUtils.ANSI_GREEN + CostantiView.PROMUOVI_UTENTE_SUCCESSO + ColorUtils.ANSI_RESET);
                } else {
                    ViewUtils.println(ColorUtils.ANSI_RED + CostantiView.PROMUOVI_UTENTE_ERRORE + ColorUtils.ANSI_RESET);
                }

            } catch (CommandException e) {
                // Gestisce il comando /b o /back
                ViewUtils.println(CostantiView.OPERAZIONE_ANNULLATA);
            }
        }
        InputUtils.pressEnterToContinue(CostantiView.A_CAPO_INVIO_PER_MENU);
    }

    /**
     * Gestisce il flusso guidato e sicuro per la rimozione del ruolo di Capo Progetto,
     * forzando la riassegnazione delle responsabilità come da regola aziendale RA3.
     */
    private void handleDegradaCapoProgetto() {
        ViewUtils.clearScreen();
        ViewUtils.println(ColorUtils.ANSI_BOLD + CostantiView.RIMUOVI_RUOLO_CAPO_PROGETTO + ColorUtils.ANSI_RESET);
        ViewUtils.println(CostantiView.B_O_BACK);
        ViewUtils.printSeparator();

        // 1. Chiedi quale Capo Progetto degradare
        List<Utente> capiProgetto = adminController.getListaCapiProgetto();
        if (capiProgetto.isEmpty()) {
            ViewUtils.println(CostantiView.DEGRADA_RUOLO_NO_CAPO_PROGETTO_DA_RIMUOVERE);
            InputUtils.pressEnterToContinue(CostantiView.A_CAPO_INVIO_PER_MENU);
            return;
        }

        // Aggiungiamo il blocco try-catch per gestire la CommandException di InputUtils
        try {
            ViewUtils.println(CostantiView.DEGRADA_RUOLO_LISTA_CAPI_PROGETTO);
            capiProgetto.forEach(u -> ViewUtils.println(String.format(CostantiView.FORMATO_NOME_COMPLETO_UTENTE, u.getIdUtente(), u.getNome(), u.getCognome())));
            ViewUtils.printSeparator();

            int idDaDegradare = InputHelper.chiediIdValido(CostantiView.DEGRADA_RUOLO_ID_CAPO_PROGETTO, capiProgetto);

            final int finalIdDaDegradare = idDaDegradare;
            List<Utente> altriCapi = capiProgetto.stream()
                    .filter(cp -> cp.getIdUtente() != finalIdDaDegradare)
                    .toList();

            if (altriCapi.isEmpty() && !adminController.getProgettiDiUnResponsabile(idDaDegradare).isEmpty()) {
                ViewUtils.println(ColorUtils.ANSI_RED + CostantiView.RIASSEGNAZIONE_ERRORE + ColorUtils.ANSI_RESET);
                InputUtils.pressEnterToContinue(CostantiView.A_CAPO_INVIO_PER_MENU);
                return;
            }

            // 3. Trova e gestisci i progetti dell'utente da degradare
            List<Progetto> progettiDaRiassegnare = adminController.getProgettiDiUnResponsabile(idDaDegradare);
            if (!progettiDaRiassegnare.isEmpty()) {
                ViewUtils.println(ColorUtils.ANSI_YELLOW + CostantiView.DEGRADA_RUOLO_WARNING + ColorUtils.ANSI_RESET);
                ViewUtils.println(CostantiView.DEGRADA_RUOLO_RIASSEGNAZIONE_NECESSARIA);

                for (Progetto progetto : progettiDaRiassegnare) {
                    ViewUtils.printSeparator();
                    ViewUtils.println("Riassegnazione del progetto: '" + progetto.getNomeProgetto() + "' (ID: " + progetto.getIdProgetto() + ")");
                    ViewUtils.println(CostantiView.DEGRADA_RUOLO_CAPI_PROGETTO_DISPONIBILI);
                    altriCapi.forEach(u -> ViewUtils.println(String.format(CostantiView.FORMATO_NOME_UTENTE, u.getIdUtente(), u.getNome())));

                    int idNuovoResponsabile = InputHelper.chiediIdValido(CostantiView.DEGRADA_RUOLO_INSERISCI_ID_NUOVO_CAPO_PROGETTO, altriCapi);

                    if (adminController.riassegnaProgetto(progetto.getIdProgetto(), idNuovoResponsabile)) {
                        ViewUtils.println(ColorUtils.ANSI_GREEN + CostantiView.RIASSEGNAZIONE_SUCCESSO + ColorUtils.ANSI_RESET);
                    } else {
                        ViewUtils.println(ColorUtils.ANSI_RED + CostantiView.RIASSEGNAZIONE_ERRORE_2 + ColorUtils.ANSI_RESET);
                        InputUtils.pressEnterToContinue("");
                        return; // Interrompe l'intera operazione se una riassegnazione fallisce
                    }
                }
                ViewUtils.println(ColorUtils.ANSI_GREEN + CostantiView.DEGRADA_RUOLO_SUCCESSO_2 + ColorUtils.ANSI_RESET);
            } else {
                ViewUtils.println(CostantiView.DEGRADA_RUOLO_NO_RESPONSABILITA);
            }

            // 4. Esegui il degrado finale
            ViewUtils.println("Procedo con il degrado del ruolo...");
            if (adminController.finalizzaDegradoCapoProgetto(idDaDegradare)) {
                ViewUtils.println(ColorUtils.ANSI_GREEN + CostantiView.DEGRADA_RUOLO_SUCCESSO + ColorUtils.ANSI_RESET);
            } else {
                ViewUtils.println(ColorUtils.ANSI_RED + CostantiView.DEGRADA_RUOLO_ERRORE + ColorUtils.ANSI_RESET);
            }

        } catch (CommandException e) {
            // Se l'utente digita /b o /back in qualsiasi momento, l'operazione viene annullata.
            ViewUtils.println(CostantiView.OPERAZIONE_ANNULLATA);
        }
        InputUtils.pressEnterToContinue(CostantiView.INVIO_PER_MENU);
    }

    /**
     * Gestisce il flusso per l'aggiunta di un nuovo progetto.
     */
    private void handleAggiungiProgetto() {
        ViewUtils.clearScreen();
        ViewUtils.println(ColorUtils.ANSI_BOLD + CostantiView.AGGIUNGI_PROGETTO + ColorUtils.ANSI_RESET);
        ViewUtils.println(CostantiView.B_O_BACK);
        ViewUtils.printSeparator();

        try {
            String nomeProgetto = InputUtils.askForInput(CostantiView.AGGIUNGI_PROGETTO_NOME_PROGETTO);
            String descrizioneProgetto = InputUtils.askForInput(CostantiView.AGGIUNGI_PROGETTO_DESCRIZIONE_PROGETTO);

            if (adminController.creaNuovoProgetto(nomeProgetto, descrizioneProgetto)) {
                ViewUtils.println(ColorUtils.ANSI_GREEN + "\nProgetto '" + nomeProgetto + "' creato con successo!" + ColorUtils.ANSI_RESET);
                ViewUtils.println("Ora puoi assegnarlo a un Capo Progetto usando l'opzione 3.");
            } else {
                ViewUtils.println(ColorUtils.ANSI_RED + CostantiView.AGGIUNGI_PROGETTO_IMPOSSIBILE_CREARE_PROGETTO + ColorUtils.ANSI_RESET);
            }
        } catch (CommandException e) {
            ViewUtils.println(CostantiView.OPERAZIONE_ANNULLATA);
        }

        InputUtils.pressEnterToContinue(CostantiView.INVIO_PER_MENU);
    }

    /**
     * Gestisce il flusso per assegnare un progetto non assegnato a un Capo Progetto (AM3).
     * Riutilizza il metodo helper 'chiediIdValido' per una validazione dell'input robusta.
     */
    private void handleAssegnaResponsabilita() {
        ViewUtils.clearScreen();
        ViewUtils.println(ColorUtils.ANSI_BOLD + CostantiView.ASSEGNA_RESPONSABILITA + ColorUtils.ANSI_RESET);
        ViewUtils.println(CostantiView.B_O_BACK);
        ViewUtils.printSeparator();

        // 1. Recupera i dati necessari
        List<Progetto> progetti = adminController.getListaProgettiNonAssegnati();
        if (progetti.isEmpty()) {
            ViewUtils.println(CostantiView.ASSEGNAZIONE_NESSUN_PROGETTO_DA_ASSEGNARE);
            InputUtils.pressEnterToContinue(CostantiView.A_CAPO_INVIO_PER_MENU);
            return;
        }

        List<Utente> capiProgetto = adminController.getListaCapiProgetto();
        if (capiProgetto.isEmpty()) {
            ViewUtils.println(CostantiView.ASSEGNAZIONE_NO_CAPI_PROGETTO_DISPONIBILI);
            InputUtils.pressEnterToContinue(CostantiView.A_CAPO_INVIO_PER_MENU);
            return;
        }

        try {
            // 2. Mostra i progetti e chiede una selezione VALIDA
            ViewUtils.println(CostantiView.ASSEGNAZIONE_LISTA_PROGETTI_NON_ASSEGNATI);
            progetti.forEach(p -> ViewUtils.println(String.format(CostantiView.FORMATO_ID_NOME, p.getIdProgetto(), p.getNomeProgetto())));
            ViewUtils.printSeparator();
            int idProgetto = InputHelper.chiediIdValido(CostantiView.ASSEGNAZIONE_INSERIRE_ID_PROGETTO, progetti);


            // 3. Mostra i capi progetto e chiede una selezione VALIDA
            ViewUtils.println(CostantiView.ASSEGNAZIONE_LISTA_CAPI_PROGETTO);
            capiProgetto.forEach(cp -> ViewUtils.println(String.format(CostantiView.FORMATO_ID_NOME_COGNOME, cp.getIdUtente(), cp.getNome(), cp.getCognome())));
            ViewUtils.printSeparator();
            int idCapo = InputHelper.chiediIdValido(CostantiView.ASSEGNAZIONE_INSERIRE_ID_CAPO_PROGETTO, capiProgetto);

            // 4. Esegue l'azione solo dopo aver ricevuto input validati
            if (adminController.assegnaProgetto(idProgetto, idCapo)) {
                ViewUtils.println(ColorUtils.ANSI_GREEN + CostantiView.ASSEGNAZIONE_PROGETTO_ASSEGNATO + ColorUtils.ANSI_RESET);
            } else {
                ViewUtils.println(ColorUtils.ANSI_RED + CostantiView.ASSEGNAZIONE_PROGETTO_NON_ASSEGNATO + ColorUtils.ANSI_RESET);
            }

        } catch (CommandException e) {
            ViewUtils.println(CostantiView.OPERAZIONE_ANNULLATA);
        }

        InputUtils.pressEnterToContinue(CostantiView.INVIO_PER_MENU);
    }

    /**
     * Gestisce il flusso guidato per riassegnare un progetto da un Capo Progetto a un altro (AM4).
     * Esclude il responsabile attuale dalla lista dei nuovi candidati.
     */
    private void handleRiassegnaProgetto() {
        ViewUtils.clearScreen();
        ViewUtils.println(ColorUtils.ANSI_BOLD + CostantiView.RIASSEGNA_RESPONSABILITA + ColorUtils.ANSI_RESET);
        ViewUtils.println(CostantiView.B_O_BACK);
        ViewUtils.printSeparator();

        // 1. Recupera i dati necessari
        List<ProgettoResponsabileDTO> progettiAssegnati = adminController.getListaProgettiConResponsabile();
        if (progettiAssegnati.isEmpty()) {
            ViewUtils.println(CostantiView.RIASSEGNAZIONE_NO_PROGETTI_ASSEGNATI);
            InputUtils.pressEnterToContinue(CostantiView.A_CAPO_INVIO_PER_MENU);
            return;
        }

        List<Utente> tuttiCapiProgetto = adminController.getListaCapiProgetto();
        // Se c'è meno di un altro capo progetto, la riassegnazione è impossibile.
        if (tuttiCapiProgetto.size() < 2) {
            ViewUtils.println(ColorUtils.ANSI_YELLOW + CostantiView.RIASSEGNAZIONE_NO_ABBASTANZA_CAPI_PROGETTO + ColorUtils.ANSI_RESET);
            InputUtils.pressEnterToContinue(CostantiView.A_CAPO_INVIO_PER_MENU);
            return;
        }

        try {
            // 2. Mostra i progetti e chiede una selezione valida
            ViewUtils.println(CostantiView.RIASSEGNAZIONE_LISTA_PROGETTI);
            progettiAssegnati.forEach(dto -> {
                String riga = String.format(CostantiView.FORMATO_ID_NOME_NOME_COGNOME,
                        dto.getIdProgetto(), dto.getNomeProgetto(), dto.getNomeResponsabile(), dto.getCognomeResponsabile());
                ViewUtils.println(riga);
            });
            ViewUtils.printSeparator();

            int idProgetto = InputHelper.chiediIdValido(CostantiView.RIASSEGNAZIONE_INSERISCI_ID_PROGETTO, progettiAssegnati);

            // 3. Identifica il responsabile attuale e filtra la lista dei candidati
            final int idResponsabileAttuale = progettiAssegnati.stream()
                    .filter(p -> p.getIdProgetto() == idProgetto)
                    .findFirst()
                    .map(ProgettoResponsabileDTO::getIdResponsabile) // Grazie al DTO modificato!
                    .orElseThrow(() -> new IllegalStateException(CostantiView.RIASSEGNAZIONE_ERRORE_4));

            List<Utente> capiProgettoDisponibili = tuttiCapiProgetto.stream()
                    .filter(cp -> cp.getIdUtente() != idResponsabileAttuale)
                    .toList();

            if (capiProgettoDisponibili.isEmpty()) {
                ViewUtils.println(ColorUtils.ANSI_RED + CostantiView.RIASSEGNAZIONE_NESSUN_CAPO_PROGETTO_DISPONIBILE + ColorUtils.ANSI_RESET);
                InputUtils.pressEnterToContinue(CostantiView.A_CAPO_INVIO_PER_MENU);
                return;
            }

            // 4. Mostra la lista filtrata e chiede la nuova selezione
            ViewUtils.println(CostantiView.RIASSEGNAZIONE_LISTA_CAPI_PROGETTO_DISPONIBILI);
            capiProgettoDisponibili.forEach(cp -> ViewUtils.println(String.format(CostantiView.FORMATO_ID_NOME_COGNOME, cp.getIdUtente(), cp.getNome(), cp.getCognome())));
            ViewUtils.printSeparator();

            // La validazione avviene sulla lista dei candidati disponibili
            int idNuovoCapo = InputHelper.chiediIdValido(CostantiView.RIASSEGNAZIONE_ID_NUOVO_CAPO_PROGETTO, capiProgettoDisponibili);

            // 5. Esegue l'azione
            if (adminController.riassegnaProgetto(idProgetto, idNuovoCapo)) {
                ViewUtils.println(ColorUtils.ANSI_GREEN + CostantiView.RIASSEGNAZIONE_OK + ColorUtils.ANSI_RESET);
            } else {
                ViewUtils.println(ColorUtils.ANSI_RED + CostantiView.RIASSEGNAZIONE_ERRORE_3 + ColorUtils.ANSI_RESET);
            }

        } catch (CommandException e) {
            ViewUtils.println(CostantiView.INVIO_PER_MENU);
        }

        InputUtils.pressEnterToContinue(CostantiView.INVIO_PER_MENU);
    }
}
