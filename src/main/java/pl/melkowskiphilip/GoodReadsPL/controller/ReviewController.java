package pl.melkowskiphilip.GoodReadsPL.controller;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reviews")
@Transactional(readOnly = true)
public class ReviewController {
}
