# D6 - Versione 1.0 del codice sorgente

## Gruppo: Proscutto

- Capo gruppo : Filippo Zangrossi (896101)
- Altri membri:
    - Giulio Furlan (896140)
    - Giovanni Marola (887931)

## Stato di avanzamento dello sviluppo

È stata implementata la struttura di base dell'applicazione, con le tab principali e in ciascuna
sono state implementate le funzionalità di base.

### Stato avanzamento singole parti

- 4 tab principali
    - **home**: implementata la UI di base con un esempio.\
            *Prossimo passo*: sincronizzazione con i dati degli utenti presenti su firebase
    - **impostazioni**: implementato task *logout*\
            *Prossimo passo*: aggiungere altre impostazioni e reindirizzare l'utente alla 
            schermata di *login* quando viene fatto il *logout*
    - **task**: implementata la UI di base che salva in locale la scelta delle task dell'utente e il loro completamento\
            *Prossimo passo*: sincronizzazione delle task scelte dagli utenti con il database
            di firebase, implementazione della funzionalità relativa agli amici, implementazione
            delle statistiche relative alle task
    - **quiz**: implementata la ui di base con un esempio di svolgimento dei quiz giornalieri 1.0 .\
            *Prossimo passo*: sincronizzazione dei quiz con il database di firebase, salvando i
            risultati dei quiz non più in locale ma legandoli all account e facendo in modo che si
            riscontrino dei progressi sul proprio personaggio
- **pagina negozio**: *ancora da implementare*
- **pagina social**: *ancora da implementare*
- **pagine di autenticazione**: terminata implementazione *login* e *signup*\
    *Prossimo passo*: implementazione del sistema *password dimenticata*
