package com.chatmulticanale.view;

import com.chatmulticanale.controller.InterazioneUtenteController;
import com.chatmulticanale.exception.CommandException;
import com.chatmulticanale.utils.*;
import com.chatmulticanale.view.costanti_view.CostantiView;
import com.chatmulticanale.view.navigation.Navigazione;
import com.chatmulticanale.view.navigation.View;

/**
 * Schermata principale per utenti con ruolo Dipendente.
 * <p>
 * Consente di accedere ai canali di progetto e alle chat private tramite
 * il flusso di comunicazione gestito da {@link ComunicazioneViewHelper}.
 */
public class DipendenteView implements View {

    /** Controller per gestire l'interazione con canali e chat. */
    private final InterazioneUtenteController interazioneController;
    /** Helper per visualizzazione e invio messaggi in diverse viste. */
    private final ComunicazioneViewHelper comunicazioneHelper;

    /**
     * Costruisce la vista per il dipendente.
     *
     * @param interazioneController controller per interazioni utente-chat
     */
    public DipendenteView(InterazioneUtenteController interazioneController) {
        this.interazioneController = interazioneController;
        this.comunicazioneHelper = new ComunicazioneViewHelper(interazioneController);
    }

    /**
     * Mostra il menu principale del dipendente e gestisce la navigazione.
     * <p>
     * Supporta logout e accesso all'area comunicazioni.
     *
     * @return navigazione successiva (logout/back)
     */
    @Override
    public Navigazione show() {
        while (true) {
            ViewUtils.clearScreen();
            ViewUtils.println(ColorUtils.ANSI_BOLD + CostantiView.HOME_DIPENDENTE + ColorUtils.ANSI_RESET);
            ViewUtils.println(CostantiView.WELCOME + SessionManager.getInstance().getUtenteLoggato().getUsername() + "!");
            ViewUtils.printSeparator();
            ViewUtils.println(CostantiView.HOME_DIPENDENTE_OPZIONE_1);
            ViewUtils.println(CostantiView.HOME_DIPENDENTE_OPZIONE_0);
            ViewUtils.printSeparator();

            try {
                int scelta = InputUtils.readIntInRange(CostantiView.SELEZIONA_OPZIONE, 0, 1);
                switch (scelta) {
                    case 1 -> handleAccediACanaliEChat();
                    case 0 -> {
                        SessionManager.getInstance().logout();
                        return Navigazione.logout();
                    }
                }
            } catch (CommandException e) {
                if (e.getNavigazione().azione == Navigazione.Azione.LOGOUT) {
                    return e.getNavigazione();
                }
                ViewUtils.println(ColorUtils.ANSI_RED + "Errore: Inserisci un numero valido." + ColorUtils.ANSI_RESET);
                InputUtils.pressEnterToContinue(CostantiView.INVIO_PER_RIPROVARE);
            }
        }
    }

    /**
     * Gestisce l'accesso all'area comunicazioni del dipendente.
     * <p>
     * Permette di scegliere tra accesso ai canali di progetto e alle chat private.
     */
    private void handleAccediACanaliEChat() {
        while (true) {
            ViewUtils.clearScreen();
            ViewUtils.println(ColorUtils.ANSI_BOLD + CostantiView.AREA_COMUNICAZIONI + ColorUtils.ANSI_RESET);
            ViewUtils.println(CostantiView.B_O_BACK_2);
            ViewUtils.printSeparator();
            ViewUtils.println(CostantiView.ACCEDI_A_CANALI_E_CHAT_OPZIONE_1);
            ViewUtils.println(CostantiView.ACCEDI_A_CANALI_E_CHAT_OPZIONE_2);
            ViewUtils.printSeparator();

            try {
                int scelta = InputUtils.readIntInRange(CostantiView.SELEZIONA_OPZIONE, 1, 2);
                int idUtente = SessionManager.getInstance().getUtenteLoggato().getIdUtente();

                switch (scelta) {
                    case 1 -> comunicazioneHelper.handleAccessoCanaliProgetto(idUtente);
                    case 2 -> comunicazioneHelper.handleAccessoChatPrivate(idUtente);
                }
            } catch (CommandException e) {
                return; // Torna alla vista chiamante
            }
        }
    }
}