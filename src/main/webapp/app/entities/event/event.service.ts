import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IEvent } from 'app/shared/model/event.model';

type EntityResponseType = HttpResponse<IEvent>;
type EntityArrayResponseType = HttpResponse<IEvent[]>;

@Injectable({ providedIn: 'root' })
export class EventService {
    public resourceUrl = SERVER_API_URL + 'api/events';

    constructor(private http: HttpClient) {}

    create(event: IEvent): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(event);
        return this.http
            .post<IEvent>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(event: IEvent): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(event);
        return this.http
            .put<IEvent>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IEvent>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IEvent[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    protected convertDateFromClient(event: IEvent): IEvent {
        const copy: IEvent = Object.assign({}, event, {
            timestamp: event.timestamp != null && event.timestamp.isValid() ? event.timestamp.toJSON() : null,
            eventStart: event.eventStart != null && event.eventStart.isValid() ? event.eventStart.toJSON() : null,
            eventEnd: event.eventEnd != null && event.eventEnd.isValid() ? event.eventEnd.toJSON() : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.timestamp = res.body.timestamp != null ? moment(res.body.timestamp) : null;
            res.body.eventStart = res.body.eventStart != null ? moment(res.body.eventStart) : null;
            res.body.eventEnd = res.body.eventEnd != null ? moment(res.body.eventEnd) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((event: IEvent) => {
                event.timestamp = event.timestamp != null ? moment(event.timestamp) : null;
                event.eventStart = event.eventStart != null ? moment(event.eventStart) : null;
                event.eventEnd = event.eventEnd != null ? moment(event.eventEnd) : null;
            });
        }
        return res;
    }
}
