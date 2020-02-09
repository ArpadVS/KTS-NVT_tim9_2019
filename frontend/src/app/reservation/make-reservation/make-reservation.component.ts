import { Component, OnInit } from '@angular/core';
import { Hall, SittingSector } from 'src/app/shared/models/hall.model';
import { Event } from 'src/app/shared/models/event.model';
import { EventSectorDTO } from 'src/app/shared/models/create-event.model';
import { EventSector } from 'src/app/shared/models/event-sector.model';
import { FormBuilder } from '@angular/forms';
import { Router } from '@angular/router';
import { LocationService } from 'src/app/core/services/location.service';
import { EventService } from 'src/app/core/services/event.service';
import { ToastrService } from 'ngx-toastr';
import { ReservationDTO, SittingTicketDTO } from 'src/app/shared/models/reservation.model';

@Component({
  selector: 'app-make-reservation',
  templateUrl: './make-reservation.component.html',
  styleUrls: ['./make-reservation.component.scss']
})
export class MakeReservationComponent implements OnInit {

  event: Event;
  eventSectors: EventSector[];
  reservation: ReservationDTO;
  constructor(
    private fb: FormBuilder,
    private router: Router,
    private locationService: LocationService,
    private eventService: EventService,
    private toastr: ToastrService
  ) {
    this.reservation = {
      tickets: [],
      purchased: false,
      eventDayId: ''
    };
  }

  ngOnInit() {
    this.eventService.get(localStorage.getItem('selectedEvent')).subscribe(
      result => {
        console.log(result);
        this.event = result;
        this.eventSectors = result.eventSectors;
      }
    );
  }

  processReservation(obj) {
    console.log(obj);
    this.reservation.eventDayId = localStorage.getItem('selectedEventDay');
    this.reservation.purchased = false;
    obj.seatObjects.forEach( x => {
      const sitTicket: SittingTicketDTO = {
        type: 'sitting',
        eventSectorId: x.sectorId,
        row: x.row,
        col: x.col
      };
      this.reservation.tickets.push(sitTicket);
    });
    console.log(this.reservation);
  }
}
