package ru.yandex.practicum.filmorate.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import ru.yandex.practicum.filmorate.dal.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.dal.storage.genre.GenreDBStorage;
import ru.yandex.practicum.filmorate.dal.storage.likes.LikeDBStorage;
import ru.yandex.practicum.filmorate.dal.storage.mpa.MpaDBStorage;

import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.dto.MpaDto;

import ru.yandex.practicum.filmorate.exception.BadRequestException;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.mapper.MpaMapper;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;



import java.time.LocalDate;
import java.util.List;



@Slf4j
@Service

public class FilmDBService {

    private final FilmStorage filmDBStorage;
    private final GenreDBStorage genreDBStorage;
    private final LikeDBStorage likeDBStorage;
    private final UserDBService userDBService;
    private final MpaDBStorage mpaDBStorage;

    //внедряем экземпляр FilmDBStorage, т.к. интерфейс может реализовывать несколько классов
    public FilmDBService(@Qualifier("FilmDBStorage") FilmStorage filmDBStorage, GenreDBStorage genreDBStorage,
                         UserDBService userDBService, LikeDBStorage likeDBStorage,
                         MpaDBStorage mpaDBStorage) {
        this.filmDBStorage = filmDBStorage;
        this.genreDBStorage = genreDBStorage;
        this.userDBService = userDBService;
        this.likeDBStorage = likeDBStorage;
        this.mpaDBStorage = mpaDBStorage;

    }

    private static final LocalDate release = LocalDate.of(1895, 12, 28);

    public FilmDto createFilm(Film film) { //добавить добавление в базу рейтингов
        checkForCreate(film);
        setMpa(film);
        setGenre(film);
        return FilmMapper.mapToFilmDto(filmDBStorage.createFilm(film));
    }

    public FilmDto updateFilm(Film film) {
        checkForUpdate(film);
        setMpa(film);
        setGenre(film);
        return FilmMapper.mapToFilmDto(filmDBStorage.updateFilm(film));
    }

    public List<FilmDto> getAllFilms() {
        return listFilmToListDto(filmDBStorage.getAllFilms());

    }

    public void addLike(int filmId, int userId) {
        userDBService.checkUserId(userId);
        likeDBStorage.addLikeToFilm(filmId, userId);
    }

    public void unlike(int filmId, int userId) {
        userDBService.checkUserId(userId);
        likeDBStorage.deleteLike(filmId, userId);
    }

    public List<FilmDto> popularFilm(String count) {
        return listFilmToListDto(filmDBStorage.popularFilm(count));
    }

    public List<MpaDto> getAllMpa() {
        return mpaDBStorage.getAllMpa().stream()
                .map(MpaMapper::mapToMpaDto)
                .toList();
    }

    public MpaDto getMpaById(int id) {
        if (mpaDBStorage.findMpaByID(id).isEmpty()) {
            log.error("Пользователь ввел неверный id");
            throw new ValidationException("Неверный id mpa");
        }
        return MpaMapper.mapToMpaDto(mpaDBStorage.findMpaByID(id).get());
    }
    public List<GenreDto>getAllGenre(){
        return genreDBStorage.getAllGenre().stream()
                .map(GenreMapper::mapToGenreDto)
                .toList();
    }

    public void checkForCreate(Film film) {
        checkDate(film);
    }

    public void checkDate(Film film) {
        if (!isDateNull(film.getReleaseDate()) && film.getReleaseDate().isBefore(release)) {
            log.error("Пользователь ввел дату ранее 28.12.1895");
            throw new BadRequestException("Дата релиза не может быть раньше 28.12.1985");
        }
    }

    public boolean isDateNull(LocalDate date) {
        return date == null;
    }

    public void checkForUpdate(Film film) {
        if (isIdNull(film.getId())) {
            log.error("Пользователь не ввел id");
            throw new ValidationException("Id должен быть указан");
        }
        if (filmDBStorage.findFilmByID(film.getId()).isPresent()) {
            checkDate(film);
        } else {
            log.error("Фильм с= " + film.getId() + " не найден");
            throw new ValidationException("Фильм с id = " + film.getId() + " не найден");
        }
    }

    public boolean isIdNull(int id) {
        return id == 0;
    }

    public Film setMpa(Film film) {
        if (film.getMpa() != null) {
            int mpaId = film.getMpa().getId();
            if (mpaDBStorage.findMpaByID(mpaId).isPresent()) {
                film.setMpa(mpaDBStorage.findMpaByID(mpaId).get());
                return film;
            } else {
                log.error("Введен несуществующий mpa");
                throw new BadRequestException("mpa с таким id не существует");
            }
        }
        return film;
    }

    public Film setGenre(Film film) {
        if (film.getGenres() != null) {
            List<Genre> genres = film.getGenres();
            genres = genreDBStorage.getListGenre(genres);
            if (genres.isEmpty()) {
                log.error("Введен несуществующий жанр");
                throw new BadRequestException("жанр с таким id не существует");
            }
            film.setGenres(genres);
            return film;
        }
        return film;
    }

    public List<FilmDto> listFilmToListDto(List<Film> listFilm) {
        return listFilm
                .stream()
                .map(this::setMpa)
                .map(this::setGenre)
                .map(FilmMapper::mapToFilmDto)
                .toList(); //проверить
    }

}


 /*genres.forEach(genre -> {
                if (genreDBStorage.getGenreById(genre.getId()).isPresent()) {
                    genre.setName(genreDBStorage.getGenreById(genre.getId()).get().getName());
                } else {
                    log.error("Введен несуществующий жанр");
                    throw new BadRequestException("жанр с таким id не существует");
                }
            });*/