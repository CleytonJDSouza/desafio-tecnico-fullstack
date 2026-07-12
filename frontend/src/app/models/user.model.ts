import { Address } from './address.model';

export interface User {
  id?: number;
  nome: string;
  email: string;
  telefone: string;
  enderecos: Address[];
}