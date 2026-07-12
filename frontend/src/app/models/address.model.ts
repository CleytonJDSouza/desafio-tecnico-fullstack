export interface Address {
  id?: number;
  cep: string;
  numero: string;
  rua?: string;
  estado?: string;
  cidade?: string;
  bairro?: string;
}