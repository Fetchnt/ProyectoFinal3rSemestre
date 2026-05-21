import { Component } from '@angular/core';
import { Location } from '@angular/common';

@Component({
  selector: 'app-perfil',
  standalone: false,
  templateUrl: './perfil.html',
  styleUrl: './perfil.css',
})
export class Perfil {
  constructor(private location: Location) {}

  goBack(): void {
    this.location.back();
  }
}
