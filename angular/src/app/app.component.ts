import {Component} from '@angular/core';
import {Http} from '@angular/http';

import {HttpClient} from '@angular/common/http';
import {forEach} from '@angular/router/src/utils/collection';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {




  private urlAll = 'http://localhost:9002/todos';
  private urlPending = 'http://localhost:9002/todos/pending';
  private urlDone = 'http://localhost:9002/todos/done';
  private urlSearch = 'http://localhost:9002/todos/search/'
  private texto: string;
  private data: object;
  constructor(private http: HttpClient) {
    console.log('ola ke ase');
  }

  onNameKeyUp(event: any){
    this.texto = event.target.value;
  }

  showAll() {
    this.http.get(this.urlAll).subscribe(
      (data: any[]) => {
        if (data.length) {
         this.data = data;
      } else {
        console.log('fallo');
        }
      }
    );
  }

  showPending() {
    this.http.get(this.urlPending).subscribe(
      (data: any[]) => {
        if (data.length) {
          this.data = data;
        } else {
          console.log('fallo');
        }
      }
      );
  }

  showDone() {
    this.http.get(this.urlDone).subscribe(
      (data: any[]) => {
        if (data.length) {
          this.data = data;
        } else {
        console.log('fallo');
        }
      }
      );
  }


}
