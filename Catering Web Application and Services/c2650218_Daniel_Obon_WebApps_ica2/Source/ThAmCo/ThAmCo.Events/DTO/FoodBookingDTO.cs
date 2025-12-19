using System.ComponentModel.DataAnnotations;

namespace ThAmCo.Events.DTO
{
    public class FoodBookingDTO
    {
        [Key]
        public int FoodBookingId { get; set; }

        public int ClientReferenceId { get; set; }

        public int NumberOfGuest { get; set; }

        public int MenuId { get; set; }
    }
}
