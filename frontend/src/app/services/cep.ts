import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CepConsulta } from '../models/cep-consulta.model';

@Injectable({
  providedIn: 'root'
})
export class CepService {
  private readonly apiUrl = 'http://localhost:8080/api/cep';

  constructor(private http: HttpClient) {}

  consultar(cep: string): Observable<CepConsulta> {
    return this.http.get<CepConsulta>(`${this.apiUrl}/${cep}`);
  }
}