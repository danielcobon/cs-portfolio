using System.ComponentModel.DataAnnotations;
using System.Runtime.CompilerServices;

namespace ThAmCo.Events.Domain
{
    public class Event
    {
        [Key]
        public int EventId { get; set; }

        [Required]
        [MaxLength(50)]
        [Display(Name = "Event Name")]
        public string EventName { get; set; } = null!;

        [Required]
        [MaxLength(50)]
        [Display(Name = "Event Title")]
        public string EventTitle { get; set; } = null!;

        [Required]
        [Display(Name = "Event Date")]
        public DateTime EventDate { get; set; }

        [Required]
        [Display(Name = "Event Type")]
        public string EventTypeId { get; set; }

        [Display(Name = "Food Booking ID")]
        public int FoodBookingId { get; set; }

        [Display(Name = "Reservation ID")]
        public int ReservationId { get; set; }

        // For Soft Delete
        public bool IsDeleted { get; set; }

        // Navigation property to Staffing (many-side of one-to-many)
        public List<Staffing> Staffings { get; set; }

        // Navigation property to GuestBooking (many-side of one-to-many)
        public List<GuestBooking> GuestBookings { get; set; } 
    }
}
