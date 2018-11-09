import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IUserEventRegistration } from 'app/shared/model/user-event-registration.model';

type EntityResponseType = HttpResponse<IUserEventRegistration>;
type EntityArrayResponseType = HttpResponse<IUserEventRegistration[]>;

@Injectable({ providedIn: 'root' })
export class UserEventRegistrationService {
    public resourceUrl = SERVER_API_URL + 'api/user-event-registrations';

    constructor(private http: HttpClient) {}

    create(userEventRegistration: IUserEventRegistration): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(userEventRegistration);
        return this.http
            .post<IUserEventRegistration>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(userEventRegistration: IUserEventRegistration): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(userEventRegistration);
        return this.http
            .put<IUserEventRegistration>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IUserEventRegistration>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IUserEventRegistration[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    protected convertDateFromClient(userEventRegistration: IUserEventRegistration): IUserEventRegistration {
        const copy: IUserEventRegistration = Object.assign({}, userEventRegistration, {
            timestamp:
                userEventRegistration.timestamp != null && userEventRegistration.timestamp.isValid()
                    ? userEventRegistration.timestamp.toJSON()
                    : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.timestamp = res.body.timestamp != null ? moment(res.body.timestamp) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((userEventRegistration: IUserEventRegistration) => {
                userEventRegistration.timestamp = userEventRegistration.timestamp != null ? moment(userEventRegistration.timestamp) : null;
            });
        }
        return res;
    }
}
