import { Component } from '@angular/core';
import { Loading } from '../../services/loading';

@Component({
  selector: 'app-spinner',
  standalone: true,
  templateUrl: './spinner.html',
  styleUrl: './spinner.css'
})
export class Spinner {
  constructor(public loadingService: Loading) {}
}