using System.ComponentModel.DataAnnotations;

namespace ThAmCo.Events.Models
{
    public class EventViewModel
    {
        [Display(Name = "Event ID")]
        public int EventId { get; set; }

        [Display(Name = "Event Name")]
        public string EventName { get; set; }

        [Display(Name = "Event Title")]
        public string EventTitle { get; set; }

        [Display(Name = "Date")]
        public DateTime EventDate { get; set; }

        [Display(Name = "Event Type")]
        public int EventTypeId { get; set; }
    }
}
