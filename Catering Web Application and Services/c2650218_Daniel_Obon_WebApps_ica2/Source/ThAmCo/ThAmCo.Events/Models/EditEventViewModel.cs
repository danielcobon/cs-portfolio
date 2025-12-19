using Microsoft.AspNetCore.Mvc.Rendering;
using System.ComponentModel.DataAnnotations;

namespace ThAmCo.Events.Models
{
    public class EditEventViewModel
    {
            // Other properties that you might need to display/edit

            public int EventId { get; set; }

            [Required]
            [MaxLength(50)]
            public string EventName { get; set; }

            [Required]
            [MaxLength(50)]
            public string EventTitle { get; set; }

            [Required]
            public DateTime EventDate { get; set; }

            [Required]
            public string EventTypeId { get; set; }

            public int FoodBookingId { get; set; }

            public int ReservationId { get; set; }

            // Property for dropdown list options
            public SelectList EventTypes { get; set; }

            // Property for dropdown list of guests
            public SelectList Guests { get; set; }
        }

}
