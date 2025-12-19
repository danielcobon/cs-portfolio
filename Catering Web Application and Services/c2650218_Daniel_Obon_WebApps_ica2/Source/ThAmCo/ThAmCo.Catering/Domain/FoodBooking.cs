using System.ComponentModel.DataAnnotations;

namespace ThAmCo.Catering.Domain
{
    public class FoodBooking
    {
        [Key]
        public int FoodBookingId { get; set; }

        public int ClientReferenceId { get; set; }

        public int NumberOfGuest {  get; set; }

        public int MenuId { get; set; }

        // Navigation property to Menu (one-side of one-to-many)
        public Menu Menu { get; set; }
    }
}
