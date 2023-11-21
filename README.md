# ASD-Golino-Paolini

## Autori
Giacomo Golino (Matr. 719210)
Andrea Paolini (Matr. 724014)


### Avvio del programma
All'avvio del programma compare a video una _GUI_ _Graphic User Interface_ che richiede in input diversi valori quali
* _Altezza_ della griglia: numero di _righe_ che deve avere la nostra griglia.
* _Larghezza_ della griglia: numero di _colonne_ che deve avere la nostra griglia.
* _Percentuale_ delle celle non traversabili: numero che deve essere compreso fra ]0;100] rappresentante la percentuale delle celle che non possono essere attraversate.
* Fattore di _agglomerazione_: numero indicante il numero massimo di celle non attraversabili adiacenti'
* Numero di _agenti_: numero indicante quanti agenti possono essere creati.

### Calcolo del numero di celle non attraversabili tramite percentuale
Il calcolo edl numero di _celle **non** attraversabili_ all'interno del nostro grafo viene calcolato tramite la segguente funzione matematica:

<p align="center">
$\frac{(altezza * larghezza * percentuale)}{100}$
</p>

### Stato delle celle
All'interno del nostro programma, per indicare lo stato di una cella, abbiamo deciso di utilizzare dei valori interi:
* **0**: la cella può essere attraversata.
* **1**: la cella **non** può essere attraversata.
* **2**: la cella è l'_init_ di un agent.
* **3**: la cella è un _goal_ di un agent.
