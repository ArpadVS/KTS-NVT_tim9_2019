import { Injectable } from '@angular/core';

@Injectable()
export class ConstantsService {
    readonly localhost = 'http://localhost:8080';
    readonly authenticationPath = 'http://localhost:8080/auth';
    readonly chartsPath = 'http://localhost:8080/api/charts';
    readonly userPath = 'http://localhost:8080/api/user';
}

