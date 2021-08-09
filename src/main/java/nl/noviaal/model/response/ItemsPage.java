package nl.noviaal.model.response;

import nl.noviaal.domain.Item;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public record ItemsPage(long total, int pages, int current, List<ItemResponse> items) {
  public static ItemsPage from(Page<Item> page) {
    return new ItemsPage(
      page.getTotalElements(),
      page.getTotalPages(),
      page.getNumber(),
      page.get().map(ItemResponse::from).collect(Collectors.toList())
    );
  }

  @Override
  public String toString() {
    return this.getClass().getName() + 
           "(items: " + items.size() + "/" + total + 
           ", page: " + current + " of " + pages + 
           ")";
  }
}
