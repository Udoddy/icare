import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { addCurrentPatient } from '../../../store/actions';
import { AppState } from '../../../store/reducers';
import { Patient } from '../../resources/patient/models/patient.model';
import { PatientService } from '../../resources/patient/services/patients.service';

@Component({
  selector: 'app-patient-search',
  templateUrl: './patient-search.component.html',
  styleUrls: ['./patient-search.component.scss'],
})
export class PatientSearchComponent implements OnInit {
  patients$: Observable<any>;
  searching: boolean;
  showList: boolean;
  @Output() selectPatient: EventEmitter<Patient> = new EventEmitter<Patient>();

  constructor(
    private patientService: PatientService,
    private store: Store<AppState>
  ) {}

  ngOnInit(): void {}

  onSearchPatients(e): void {
    if (e) {
      e.stopPropagation();
      this.searching = true;
      this.showList = false;
      this.patients$ = this.patientService.getPatients(e.target.value).pipe(
        tap(() => {
          this.searching = false;
          this.showList = true;
        })
      );
    }
  }

  onSelectPatient(e, patient: Patient): void {
    e.stopPropagation();
    this.showList = false;
    this.store.dispatch(addCurrentPatient({ patient }));
    this.selectPatient.emit(patient);
  }
}