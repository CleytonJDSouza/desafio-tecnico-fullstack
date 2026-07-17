import { Injectable, signal } from '@angular/core';

export interface ToastMessage {
  id: number;
  tipo: 'sucesso' | 'erro';
  texto: string;
}

@Injectable({
  providedIn: 'root'
})
export class Toast {
  mensagens = signal<ToastMessage[]>([]);
  private proximoId = 0;

  sucesso(texto: string): void {
    this.adicionar('sucesso', texto);
  }

  erro(texto: string): void {
    this.adicionar('erro', texto);
  }

  private adicionar(tipo: 'sucesso' | 'erro', texto: string): void {
    const id = this.proximoId++;
    this.mensagens.update((lista) => [...lista, { id, tipo, texto }]);

    setTimeout(() => this.remover(id), 4000);
  }

  remover(id: number): void {
    this.mensagens.update((lista) => lista.filter((m) => m.id !== id));
  }
}