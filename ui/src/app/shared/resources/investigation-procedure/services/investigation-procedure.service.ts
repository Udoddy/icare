import { Injectable } from '@angular/core';
import { Api, OrderCreate } from '../../openmrs';
import { Observable, from, zip } from 'rxjs';
import { OpenmrsHttpClientService } from 'src/app/shared/modules/openmrs-http-client/services/openmrs-http-client.service';
import { Store } from '@ngrx/store';
import { AppState } from 'src/app/store/reducers';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class InvestigationProcedureService {
  constructor(
    private api: Api,
    private httpClientService: OpenmrsHttpClientService,
    private store: Store<AppState>
  ) {}

  saveOrder(order): Observable<OrderCreate> {
    return from(this.api.order.createOrder(order));
  }

  saveManyOrders(orders): Observable<any[]> {
    return zip(
      ...(orders || []).map((order) => {
        return this.saveOrder(order).pipe(
          map((response) => {
            return response;
          })
        );
      })
    );
  }

  deleteOrder(uuid): Observable<any> {
    return from(this.httpClientService.delete('order/' + uuid));
  }

  discontinueOrder(order): Observable<OrderCreate> {
    return from(this.api.order.createOrder(order));
  }

  saveOrdersUsingEncounter(data, encounterUuid): Observable<any> {
    return this.httpClientService.post(`encounter/${encounterUuid}`, data);
  }
}